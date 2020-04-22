package com.github.jsonldjava;

import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class has two main features:
 * 1: to parse json r4 using JSONLD-JAVA 1.1 into JSONLD.
 * 2: to parse the above JSONLD into ttl.
 */
public class GenerateFhirRDF {

    /**
     * Parse json r4 using JSONLD-JAVA 1.1 from a give directory into JSONLD.  This will create
     * a subdirectory of the given directory called jsonld.
     * @param jsonld_r4_dir
     * @param jsonld_output_dir
     * @throws Exception
     */
    public void generate_R4_Conversion_with_JSON_LD_11(String jsonld_r4_dir, String jsonld_output_dir)  throws Exception {

        // go through all JSON LD R4 files and convert with JSONLD_JAVA 1.1
        File inputFolder = new File(jsonld_r4_dir);
        String outputFolder = jsonld_r4_dir + "/" + jsonld_output_dir;
        getFolder(outputFolder);

        File[] listOfInputFiles = inputFolder.listFiles();

        int fileCount = 0;
        System.out.println("Parsing:json files to jsonld");
        for (File file : listOfInputFiles) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                //System.out.println("PARSING FILE: " + file.getName());
                parseFile(outputFolder, file);
                fileCount ++;
            }
        }
        System.out.println("Parsed " + fileCount + " json files to jsonld in " + outputFolder);
    }

    /**
     * Parse the JSONLD in the gven folder into ttl.  This will create
     * a subdirectory of the given directory called ttl.
     * @param jsonld_dir
     * @param ttl
     * @throws Exception
     */
    public void generate_ttl_with_JSON_LD(String jsonld_dir, String ttl)  throws Exception {
        // go through the JSONLD files just created and convert to RDF TTL
        File folder = new File(jsonld_dir);
        File[] listOfInputFiles = folder.listFiles();

        String outputDir = folder.getParentFile().toString() + "/" + ttl;
        getFolder(outputDir);

        int fileCount = 0;
        System.out.println("Parsing: jsonld files to ttl");
        for (File file : listOfInputFiles) {
            if (file.isFile() && file.getName().endsWith(".jsonld")) {
                //System.out.println("Parsing: " + file.getName());
                toTtl(outputDir, file);
                fileCount ++;
            }
        }
        System.out.println("Parsed " + fileCount + " jsonld files to ttl in " + outputDir);
    }

    /**
     * Parse json r4 using JSONLD-JAVA 1.1.
     * @param outputDir
     * @param jsonldFile
     */
    private void parseFile (String outputDir, File jsonldFile) {
        try {
            // Open a valid json(-ld) input file
            InputStream inputStream = new FileInputStream(jsonldFile.getAbsoluteFile());

            // Read the file into an Object (The type of this object will be a List, Map, String, Boolean,
            // Number or null depending on the root object in the file).
            Object jsonObject = JsonUtils.fromInputStream(inputStream);

            // Create a context JSON map containing prefixes and definitions
            Map context = new HashMap();

            // Create an instance of JsonLdOptions with the standard JSON-LD options
            JsonLdOptions options = new JsonLdOptions();

            // Customise options...
//            Object compact = JsonLdProcessor.compact(jsonObject, context, options);
            Object expanded = JsonLdProcessor.expand(jsonObject, options);
//            Object rdf = JsonLdProcessor.toRDF(jsonObject, options);

            String jsonString = JsonUtils.toPrettyString(expanded);
//            String rdfString = JsonUtils.toPrettyString(rdf);

            // change extension to "jsonld"
            String fileString =  jsonldFile.getName();
            writeFile(outputDir, jsonString, fileString.substring(0,fileString.lastIndexOf(".") ) + ".jsonld");

//            writeFile(outputDir, rdfString, "rdf_" + jsonldFile.getName());
//            writeFile(outputDir, rdfString,  jsonldFile.getName());

            // Print out the result (or don't, it's your call!)
            //System.out.println(JsonUtils.toPrettyString(rdf));

        } catch (Exception e){
            System.out.println(e.toString());
        }
    }

    /**
     * Convert a given jsonld file to ttl usning Apache Jena.  This will output the new .ttl file into the
     * given output directory.
     * @param outputDir
     * @param jsonldFile
     */
    private void toTtl(String outputDir, File jsonldFile) {
        try {
            Model model = ModelFactory.createDefaultModel();

            // read into the model.
            model.read(jsonldFile.getAbsolutePath()) ;

            String outputFileName =  outputDir + "/" + getBaseFileName(jsonldFile.getName() )+ ".ttl";
            FileOutputStream fos = new FileOutputStream(outputFileName);

            RDFDataMgr.write(fos, model, Lang.TURTLE);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Get the name of file without the extension.
     * @param fileString
     * @return
     */
    private static String getBaseFileName(String fileString) {
        String base = fileString.substring(0,fileString.lastIndexOf(".") );
        return base;
    }

    /**
     * Write the contents to a given file and directory.
     * @param outputDir
     * @param fileContents
     * @param fileName
     * @throws Exception
     */
    private void writeFile (String outputDir, String fileContents, String fileName) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputDir + "/" + fileName));
        writer.write(fileContents);
        writer.close();
    }

    /**
     * Verify that the folder exists.  If it doesn't, create it.
     * @param pathTofolder
     * @return
     */
    private boolean getFolder(String pathTofolder) {
        boolean exists = false;
        File folder = new File(pathTofolder);

        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("Created folder: " + folder.getName());
                exists = true;
            }
            else {
                System.out.println("ERROR: Could not create direcetory: " + folder.toString());
            }
        }

        return exists;
    }
}
