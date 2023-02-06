package project.extension.resource;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.exception.CommonException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 扫描资源拓展方法
 *
 * @author LCTR
 * @date 2023-02-05
 */
public class ScanExtension {
    public static final String DEFAULT_CLASS_PATTERN = "/**/*.class";

    public static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();

    public static final MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory(RESOURCE_PATTERN_RESOLVER);

    /**
     * 获取资源
     *
     * @param location 地址
     * @return 资源
     */
    public static Resource getResource(String location)
            throws
            CommonException {
        try {
            return RESOURCE_PATTERN_RESOLVER.getResource(location);
        } catch (Exception ex) {
            throw new CommonException(String.format("获取资源%s失败",
                                                    location),
                                      ex);
        }
    }

    /**
     * 扫描地址下的资源
     *
     * @param location 地址集合
     * @return 资源集合
     */
    public static List<Resource> scanResourceFromLocation(String location)
            throws
            CommonException {
        return scanResourceFromLocation(Collections.singletonList(location));
    }

    /**
     * 扫描地址下的资源
     *
     * @param locationList 地址集合
     * @return 资源集合
     */
    public static List<Resource> scanResourceFromLocation(List<String> locationList)
            throws
            CommonException {
        List<Resource> resourceList = new ArrayList<>();

        for (String location : locationList) {
            try {
                Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources(location);
                resourceList.addAll(Arrays.asList(resources));
            } catch (Exception ex) {
                throw new CommonException(String.format("扫描地址%s失败",
                                                        location),
                                          ex);
            }
        }

        return resourceList;
    }

    /**
     * 扫描软件包中的java类
     *
     * @param packageName 包名
     * @return java类集合
     */
    public static List<Class<?>> scanClassFromPackage(String packageName)
            throws
            CommonException {
        return scanClassFromPackage(Collections.singletonList(packageName),
                                    null,
                                    null,
                                    false,
                                    false,
                                    false);
    }

    /**
     * 扫描软件包中的java类
     *
     * @param packageName          包名
     * @param superTypeList        指定父类集合
     * @param interfaceTypeList    指定接口集合
     * @param filterAnonymousClass 过滤匿名类
     * @param filterInterface      过滤接口类
     * @param filterMemberClass    过滤成员类
     * @return java类集合
     */
    public static List<Class<?>> scanClassFromPackage(String packageName,
                                                      List<Class<?>> superTypeList,
                                                      List<Class<?>> interfaceTypeList,
                                                      boolean filterAnonymousClass,
                                                      boolean filterInterface,
                                                      boolean filterMemberClass)
            throws
            CommonException {
        return scanClassFromPackage(Collections.singletonList(packageName),
                                    superTypeList,
                                    interfaceTypeList,
                                    filterAnonymousClass,
                                    filterInterface,
                                    filterMemberClass);
    }

    /**
     * 扫描软件包中的java类
     *
     * @param packageNameList 包名集合
     * @return java类集合
     */
    public static List<Class<?>> scanClassFromPackage(List<String> packageNameList)
            throws
            CommonException {
        return scanClassFromPackage(packageNameList,
                                    null,
                                    null,
                                    false,
                                    false,
                                    false);
    }

    /**
     * 扫描软件包中的java类
     *
     * @param packageNameList      包名集合
     * @param superTypeList        指定父类集合
     * @param interfaceTypeList    指定接口集合
     * @param filterAnonymousClass 过滤匿名类
     * @param filterInterface      过滤接口类
     * @param filterMemberClass    过滤成员类
     * @return java类集合
     */
    public static List<Class<?>> scanClassFromPackage(List<String> packageNameList,
                                                      List<Class<?>> superTypeList,
                                                      List<Class<?>> interfaceTypeList,
                                                      boolean filterAnonymousClass,
                                                      boolean filterInterface,
                                                      boolean filterMemberClass)
            throws
            CommonException {
        List<Class<?>> classList = new ArrayList<>();

        for (String packageName : packageNameList) {
            try {
                //获取完整的包名匹配值
                String fullPackageName = String.format("%s%s%s",
                                                       ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX,
                                                       ClassUtils.convertClassNameToResourcePath(packageName.trim()),
                                                       DEFAULT_CLASS_PATTERN);
                //获取包内的全部资源
                Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources(fullPackageName);
                if (!CollectionsExtension.anyPlus(resources))
                    continue;

                for (Resource resource : resources) {
                    if (!resource.isReadable())
                        continue;

                    try {
                        //获取java类
                        ClassMetadata classMetadata = METADATA_READER_FACTORY.getMetadataReader(resource)
                                                                             .getClassMetadata();
                        Class<?> clazz = Class.forName(classMetadata.getClassName());

                        //过滤匿名类
                        if (filterAnonymousClass && clazz.isAnonymousClass())
                            continue;

                        //过滤接口
                        if (filterInterface && clazz.isInterface())
                            continue;

                        //过滤成员类
                        if (filterMemberClass && clazz.isMemberClass())
                            continue;

                        //过滤未继承指定父类的类
                        if (CollectionsExtension.allPlus(superTypeList,
                                                         superType -> !superType.isAssignableFrom(clazz)))
                            continue;

                        //过滤匿名类
                        if (!CollectionsExtension.allPlus(interfaceTypeList,
                                                          interfaceType -> !interfaceType.isAssignableFrom(clazz)))
                            continue;

                        classList.add(clazz);
                    } catch (Exception ignore) {

                    }
                }
            } catch (Exception ex) {
                throw new CommonException(String.format("扫描软件包%s失败",
                                                        packageName),
                                          ex);
            }
        }

        return classList;
    }
}
