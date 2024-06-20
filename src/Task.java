//Declaration of Task Class:
//  represent a task with: description, completion status, completion time
public class Task  {

    //Private fields; store task desc, completion status, completionTime
    private String description;
    private boolean completed;
    private Long completionTime;

    //Constructor: initialize new task with a given desc
    public Task(String description) {
        this.description = description;
        // Initialize a task as uncompleted
        this.completed = false;

    }

    //Methods: Task Description

        //Getter:
            // task description
    public String getDescription() {
        return description;
    }

    //Methods: Completion
        //Getter:
            //Task completion Status
    public boolean isCompleted() {
        return completed;
    }
        //Setter:
            //task completion status
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    //Methods: Completion Time
        //Getter:
            //completion time
    public Long getCompletionTime() {
        return completionTime;
    }
        //Setter:
            //completion time
    public void setCompletionTime(Long completionTime) {
        this.completionTime = completionTime;
    }


    //Override the toString Method -- provide string representation of task
    @Override
    // Return string that includes description - completion status
    public String toString() {
        return description + (completed ? " (Completed)" : "");
    }


}
