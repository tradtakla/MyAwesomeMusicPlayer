package com.appsandgamesinc.myawesomemusicplayer.model;



public class Music
{
    private String name;
    private String artist;
    private int imageId;
    private int songId;

    public Music(String name, String artist, int imageId, int songId)
    {
        this.name = name;
        this.artist = artist;
        this.imageId = imageId;
        this.songId = songId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public int getImageId()
    {
        return imageId;
    }

    public void setImageId(int imageId)
    {
        this.imageId = imageId;
    }

    public int getSongId()
    {
        return songId;
    }

    public void setSongIdId(int imageId)
    {
        this.songId = imageId;
    }

}
