import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        Model model = ModelFactory.createDefaultModel();
        model.read("dataset/SNOMEDCT.ttl");
        System.out.println("loadModel success");

        ConceptCodeService conceptCodeService = new ConceptCodeService();
        // This step test the superclasses of all the concept in value set
//        Map<String, SCTConcept> map = new HashMap<>();
//        map = conceptCodeService.findAllSuperClasses(model);
//
//        for(String key : map.keySet()) {
//            String conceptCode = map.get(key).getConceptCode();
//            String preferedName = map.get(key).getPerferedName();
//            int depth = map.get(key).getDepth();
//            System.out.println(conceptCode + ": " + preferedName + ":" + depth);
//        }
//
//        System.out.println("Depth <= 2:");
//
//        for(String key : map.keySet()) {
//            int depth = map.get(key).getDepth();
//            if(depth <= 3){
//                String conceptCode = map.get(key).getConceptCode();
//                String preferedName = map.get(key).getPerferedName();
//                System.out.println(conceptCode + ": " + preferedName + ":" + depth);
//            }
//        }

        Set<SCTConcept> children = new HashSet<>();
//        List<SCTConcept> children = new ArrayList<>();
        children = conceptCodeService.findAllSubclassesForSingle("http://purl.bioontology.org/ontology/SNOMEDCT/102915009", model);
        if(children == null) {
            System.out.println("Output is null!");
        }
        else {
            System.out.println("Children size: " + children.size());
        }
        for(SCTConcept child : children){
            System.out.println(child.getConceptCode() + " : " + child.getPerferedName());
        }

    }
}
