package exercise2.sync;

import java.util.ArrayList;
import java.util.List;

public class Method {
    private String name;
    private String Type;
    private final List<String> modifiers;

    public Method(){
        this.modifiers = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        Type = type;
    }

    public void addModifier(String modifier) {
        this.modifiers.add(modifier);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return Type;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    @Override
    public String toString() {
        return "Method{" +
                "name='" + name + '\'' +
                ", Type='" + Type + '\'' +
                ", modifiers=" + modifiers +
                '}';
    }
}
