package com.github.jsonldjava;

public class RDFGEnerator {

    public static void main(String[] args) {

        String jsonld_r4_dir;
        String rdf_output_dir;

        if (args.length != 2){
            System.out.println("Input Error"  +
                    "\n" +
                    "\nThis class takes in 2 parameters:" +
                    "\n*********************************" +
                    "\n1: Absolute directory path to the JSONLD R4 files to parse" +
                    "\n2: Existing absolute directory path to put the generated FHRI RDF files in" +
                    "\n");
            System.exit(0);
        }

        jsonld_r4_dir = args[0];
        rdf_output_dir = args[1];

        try {
            GenerateFhirRDF generator = new GenerateFhirRDF();
            generator.generate_R4_Conversion_with_JSON_LD_11(jsonld_r4_dir, rdf_output_dir);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
