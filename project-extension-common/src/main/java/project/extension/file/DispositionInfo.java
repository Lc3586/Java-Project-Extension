package project.extension.file;

/**
 * 配置信息
 *
 * @author LCTR
 * @date 2022-04-08
 */
public class DispositionInfo {
    private int Default;

    private int dub;

    private int original;

    private int comment;

    private int lyrics;

    private int karaoke;

    private int forced;

    private int hearing_Impaired;

    private int visual_Impaired;

    private int clean_Effects;

    private int attached_Pic;

    private int timed_Thumbnails;

    public int getDefault() {
        return Default;
    }

    public void setDefault(int aDefault) {
        Default = aDefault;
    }

    public int getDub() {
        return dub;
    }

    public void setDub(int dub) {
        this.dub = dub;
    }

    public int getOriginal() {
        return original;
    }

    public void setOriginal(int original) {
        this.original = original;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getLyrics() {
        return lyrics;
    }

    public void setLyrics(int lyrics) {
        this.lyrics = lyrics;
    }

    public int getKaraoke() {
        return karaoke;
    }

    public void setKaraoke(int karaoke) {
        this.karaoke = karaoke;
    }

    public int getForced() {
        return forced;
    }

    public void setForced(int forced) {
        this.forced = forced;
    }

    public int getHearing_Impaired() {
        return hearing_Impaired;
    }

    public void setHearing_Impaired(int hearing_Impaired) {
        this.hearing_Impaired = hearing_Impaired;
    }

    public int getVisual_Impaired() {
        return visual_Impaired;
    }

    public void setVisual_Impaired(int visual_Impaired) {
        this.visual_Impaired = visual_Impaired;
    }

    public int getClean_Effects() {
        return clean_Effects;
    }

    public void setClean_Effects(int clean_Effects) {
        this.clean_Effects = clean_Effects;
    }

    public int getAttached_Pic() {
        return attached_Pic;
    }

    public void setAttached_Pic(int attached_Pic) {
        this.attached_Pic = attached_Pic;
    }

    public int getTimed_Thumbnails() {
        return timed_Thumbnails;
    }

    public void setTimed_Thumbnails(int timed_Thumbnails) {
        this.timed_Thumbnails = timed_Thumbnails;
    }
}
