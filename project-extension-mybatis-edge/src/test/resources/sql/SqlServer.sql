USE [Test4Java]
GO

EXEC sys.sp_dropextendedproperty @name=N'MS_Description' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestBlobEntity'

GO

EXEC sys.sp_dropextendedproperty @name=N'MS_Description' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestBlobEntity', @level2type=N'COLUMN',@level2name=N'Bytes'

GO

EXEC sys.sp_dropextendedproperty @name=N'MS_Description' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestBlobEntity', @level2type=N'COLUMN',@level2name=N'Id'

GO

/****** Object:  Table [dbo].[TestBlobEntity]    Script Date: 2023/4/4 11:07:41 ******/
DROP TABLE [dbo].[TestBlobEntity]
GO

/****** Object:  Table [dbo].[TestBlobEntity]    Script Date: 2023/4/4 11:07:41 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[TestBlobEntity](
	[Id] [varchar](36) NOT NULL,
	[Bytes] [varbinary](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestBlobEntity', @level2type=N'COLUMN',@level2name=N'Id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文件数据' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestBlobEntity', @level2type=N'COLUMN',@level2name=N'Bytes'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'测试读写文件数据' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestBlobEntity'
GO

/****** Object:  Table [dbo].[TestClobEntity]    Script Date: 2023/4/4 11:07:51 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[TestClobEntity](
	[Id] [varchar](36) NOT NULL,
	[Text] [ntext] NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestClobEntity', @level2type=N'COLUMN',@level2name=N'Id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文本数据' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestClobEntity', @level2type=N'COLUMN',@level2name=N'Text'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'测试读写长文本数据' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestClobEntity'
GO

/****** Object:  Table [dbo].[TestGeneralEntity]    Script Date: 2023/4/4 11:08:12 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

SET ANSI_PADDING ON
GO

CREATE TABLE [dbo].[TestGeneralEntity](
	[Id] [nvarchar](36) NOT NULL,
	[Char] [nchar](1) NULL,
	[String] [nvarchar](200) NULL,
	[Byte] [tinyint] NULL,
	[Short] [smallint] NULL,
	[Integer] [int] NULL,
	[Long] [bigint] NULL,
	[Float] [real] NULL,
	[Double] [float] NULL,
	[Decimal] [decimal](38, 30) NULL,
	[Boolean] [bit] NULL,
	[Date] [date] NULL,
	[Time] [time](7) NULL,
	[Datetime] [datetime] NULL,
	[Bytes] [varbinary](max) NULL,
	[LongIdentity] [bigint] IDENTITY(1,1) NOT NULL,
 CONSTRAINT [PK__TestGene__3214EC0769FBFCFD] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO

SET ANSI_PADDING OFF
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'Id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'字符' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'Char'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'字符串' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'String'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'8位整数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'Byte'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'16位整数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'Short'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'32位整数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'Integer'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'64位整数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'Long'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'单精度浮点数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'Float'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'双精度浮点数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'Double'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'高精度浮点数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'Decimal'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'布尔' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'Boolean'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'Date'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'Time'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'日期时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'Datetime'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文件数据' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'Bytes'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'64位整数自动增长' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity', @level2type=N'COLUMN',@level2name=N'LongIdentity'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'测试读写常规数据' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestGeneralEntity'
GO

/****** Object:  Table [dbo].[TestIdentityEntity]    Script Date: 2023/4/4 11:08:25 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[TestIdentityEntity](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[No] [nvarchar](36) NULL,
PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'自增主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestIdentityEntity', @level2type=N'COLUMN',@level2name=N'Id'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestIdentityEntity', @level2type=N'COLUMN',@level2name=N'No'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'测试自增主键' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'TestIdentityEntity'
GO

