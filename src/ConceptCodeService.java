
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;


import java.util.*;

public class ConceptCodeService {

    ValueSetService valueSetService = new ValueSetService();

    // Find all super classes of the whole value set, using this method can find the top classes of the hierarchy
    public Map<String, SCTConcept> findAllSuperClasses(Model model){
        Map<String, SCTConcept> superClasses = new HashMap<>();
        List<String> codes = valueSetService.loadValueSet();
        for(String code : codes) {
            String codeURI = "http://purl.bioontology.org/ontology/SNOMEDCT/" + code;
            findSuperClassesforSingle(codeURI, superClasses, model);
        }
        return superClasses;
    }

    public void findSuperClassesforSingle(String codeURI, Map<String, SCTConcept> superClasses, Model model){

        int depth = 0;
        findSuperClassesforSingleHelper(codeURI, model, superClasses);

    }

    private int findSuperClassesforSingleHelper(String codeURI, Model model, Map<String, SCTConcept> superClasses){
        List<SCTConcept> fathers = findFathers(codeURI, model);
        int depth = -1;

        if(!fathers.isEmpty()) {
            for(SCTConcept father : fathers){
                depth = 1 + findSuperClassesforSingleHelper(father.getConceptCode(), model, superClasses);
                SCTConcept sctConcept = new SCTConcept(father.getConceptCode(), father.getPerferedName(), depth);
                superClasses.put(father.getConceptCode(), sctConcept);
            }
        }else{
            depth = 0;
        }
        return depth;
    }

    public List<SCTConcept> findFathers(String codeURI, Model model){
        List<SCTConcept> fathers = new ArrayList<>();

        String queryString =
                "prefix xsd: <http://www.w3.org/2001/XMLSchema#>" +
                        "prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#>" +
                        "prefix owl:  <http://www.w3.org/2002/07/owl#>" +
                        "prefix skos: <http://www.w3.org/2004/02/skos/core#>" +
                        "prefix umls: <http://bioportal.bioontology.org/ontologies/umls/>" +
                        "SELECT * WHERE { <" +
                        codeURI +
                        "> rdfs:subClassOf ?father ." +
                        " ?father skos:prefLabel ?label ." +
                        "}";

        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        try {
            ResultSet results = qexec.execSelect();
            while(results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Resource resource = solution.getResource("father");
                String uri = resource.getURI();
                Literal literal = solution.getLiteral("label");
                String preferedName = literal.getString();
                SCTConcept sctConcept = new SCTConcept(uri, preferedName);
                fathers.add(sctConcept);
            }
        }finally {
            qexec.close();
        }
        return fathers;
    }


    /* To find the sensitivity category of a SNOMEDCT code, one way is to find the subclasses of only the
       new code, and search them in the valueset. The second way is to find all the possible sensitive
       code (subclasses) with its sensitive category, and directly search for the new code every time when
       new code comes in.
     */

    /* This is the first way to find sensitive category of a new code: search its subclasses in the SNOMEDCT
       ttl file and find their sensitive categories in the valueset (database).
     */
    // This is the first step: finding subclasses in the SNOMEDCT.
    public Set<SCTConcept> findAllSubclassesForSingle(String codeURI, Model model) {
        Set<SCTConcept> subclasses = new HashSet<>();
        findAllSubclassesForSingleHelper(codeURI, model, subclasses);
        return subclasses;
    }

    private void findAllSubclassesForSingleHelper(String codeURI, Model model, Set<SCTConcept> subclasses) {
        Set<SCTConcept> children = findChildren(codeURI, model);

        if(!children.isEmpty()) {
            for(SCTConcept child : children){
                subclasses.add(child);
                findAllSubclassesForSingleHelper(child.getConceptCode(), model, subclasses);
            }
        }
    }


    public Set<SCTConcept> findChildren(String codeURI, Model model) {
        Set<SCTConcept> children = new HashSet<>();

        String queryString =
                "prefix xsd: <http://www.w3.org/2001/XMLSchema#>" +
                        "prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#>" +
                        "prefix owl:  <http://www.w3.org/2002/07/owl#>" +
                        "prefix skos: <http://www.w3.org/2004/02/skos/core#>" +
                        "prefix umls: <http://bioportal.bioontology.org/ontologies/umls/>" +
                        "SELECT * WHERE { ?child rdfs:subClassOf <" +
                        codeURI +
                        ">  ." +
                        " ?child skos:prefLabel ?label ." +
                        "}";

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)){
            ResultSet results = qexec.execSelect();
            while(results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                Resource resource = solution.getResource("child");
                String uri = resource.getURI();     // Get uri from resource
                Literal literal = solution.getLiteral("label");
                String preferedName = literal.getString();   // Get preferedName from resu
                children.add(new SCTConcept(uri, preferedName));  // Add SCTConcept object to the result set
            }

        }
        return children;
    }

    //
//    public Set<String> snomedSensitiveCategories(String code){
//
//        Set<String> sensitiveCategories = new HashSet<>();
//        snomedSensitiveCategoriesHelper(code, sensitiveCategories);
//        for(String category: sensitiveCategories){
//            System.out.println(category);
//        }
//
//        return sensitiveCategories;
//    }
//
//    private String snomedSensitiveCategoriesHelper(String code, Set sensitiveCategories){
//
//        List<String> fathers = findFathers(code);
//        if(!fathers.isEmpty()){
//            for(String father : fathers){
//                snomedSensitiveCategoriesHelper(father, sensitiveCategories);
//                String category =
//                if(category != null) {
//                    sensitiveCategories.add(category);
//                    return category;
//                }
//            }
//        }
//        return null;
//    }
}
