package gr.aueb;

import com.google.gson.annotations.SerializedName;

public class Cast {
    @SerializedName("name")
    private String name;
    @SerializedName("character")
    private String character;
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    
    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public String getCharacter() {
        return character;
    }
    
    @Override
    public String toString() {
        return String.format("Name: %-30s\tCharacter Name: %s", name, character + "\n");
    }
    
}