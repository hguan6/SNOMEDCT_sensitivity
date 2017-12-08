public class SCTConcept {
    private String conceptCode;
    private String perferedName;
    private int depth;


    SCTConcept(String conceptCode, int depth){
        this.conceptCode = conceptCode;
        this.depth = depth;
    }

    public SCTConcept(String conceptCode, String perferedName) {
        this.conceptCode = conceptCode;
        this.perferedName = perferedName;
    }

    public SCTConcept(String conceptCode, String perferedName, int depth) {
        this.conceptCode = conceptCode;
        this.perferedName = perferedName;
        this.depth = depth;
    }

    public String getPerferedName() {
        return perferedName;
    }

    public void setPerferedName(String perferedName){
        this.perferedName = perferedName;
    }



    public String getConceptCode() {
        return conceptCode;
    }

    public void setConceptCode(String conceptCode) {
        this.conceptCode = conceptCode;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
