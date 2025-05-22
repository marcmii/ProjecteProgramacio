package projecte;

public class Company {
    private String id;
    private String name;
    private String description;
    private String degreeId;

    public Company(String id, String name, String description, String degreeId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.degreeId = degreeId;
    }

    // Getters i setters
    public String getId() { //GETTER
    	
    	return id; 
    }
    public void setId(String id) { //SETTER
    	
    	this.id = id; 
    }

    public String getName() { //GETTER
    	
    	return name; 
    }
    
    public void setName(String name) { //SETTER
    	
    	this.name = name; 
    }

    public String getDescription() { //GETTER
    	
    	return description; 
    }
    
    public void setDescription(String description) { //SETTER
    	
    	this.description = description; 
    }

    public String getDegreeId() { //GETTER
    	
    	return degreeId; 
    }
    
    public void setDegreeId(String degreeId) { //SETTER
    	
    	this.degreeId = degreeId; 
    }
}
