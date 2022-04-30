package exercise2.lib;

import java.util.ArrayList;
import java.util.List;

public class Method {
    private String name;
    private String type;
    private int beginLine;
    private int endLine;
    private final List<String> modifiers;
    private final List<Parameter> parameters;

    public Method(){
        this.modifiers = new ArrayList<>();
        this.parameters = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBeginLine(int beginLine) {
        this.beginLine = beginLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public void addModifier(String modifier) {
        this.modifiers.add(modifier);
    }

    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getBeginLine() {
        return beginLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    @Override
    public String toString() {
        return "Method{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", beginLine:" + beginLine +
                ", endLine:" + endLine +
                ", modifiers=" + modifiers +
                ", parameters=" + parameters +
                '}';
    }
}
