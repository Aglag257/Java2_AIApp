package gr.aueb;

public class Cast {
    private String name;
    private String character;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCharacter() {
        return character;
    }
    public void setCharacter(String character) {
        this.character = character;
    }
    @Override
    public String toString() {
        return "actor: [name=" + name + ", character=" + character + "]";
    }
    
}