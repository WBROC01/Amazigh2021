package nl.brockict.amazigh2021;

public class Woord {
    public String  audiopath;
    public Integer category;
    public String  imagepath;
    public String  woordamz;
    public Integer woordid;
    public String  woordned;




    public Woord() {};

    public Woord(String audiopath, Integer category, String imagepath,
                 String woordned, Integer woordid, String woordamz) {
        this.audiopath= audiopath;
        this.category =category;
        this.imagepath= imagepath;
        this.woordamz = woordamz;
        this.woordid = woordid;
        this.woordned =woordned;



    }

}
