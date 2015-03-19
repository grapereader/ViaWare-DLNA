package ca.viaware.dlna.library.model;

import ca.viaware.api.sql.factory.obj.DatabaseObject;

import java.io.File;

public class LibraryEntry extends DatabaseObject {

    private String name;
    private int typeID;
    private int formatID;
    private int parent;

    private File location;

    public LibraryEntry(int id, String name, int typeID, int formatID, int parent, String location) {
        super(id);
        this.name = name;
        this.typeID = typeID;
        this.formatID = formatID;
        this.parent = parent;
        this.location = new File(location);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeID() {
        return typeID;
    }

    public int getFormatID() {
        return formatID;
    }

    public int getParent() {
        return parent;
    }

    public File getLocation() {
        return location;
    }
}
