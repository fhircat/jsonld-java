package com.github.jsonldjava;

public class RDFGEnerator {

    public static void main(String[] args) {

        String jsonld_r4_dir;
        String jsonld_output_dir = "jsonld";
        String ttl_output_dir = "ttl";

        if (args.length != 1){
            System.out.println("Input Error"  +
                    "\n" +
                    "\nThis class takes in 1 parameter:" +
                    "\n*********************************" +
                    "\n1: Absolute directory path to the JSONLD R4 files to parse" +
                    "\n\nThis will create two subdirectories: jsondl and ttl" +
                    "\n");
            System.exit(0);
        }

        jsonld_r4_dir = args[0];

        try {
            GenerateFhirRDF generator = new GenerateFhirRDF();
            generator.generate_R4_Conversion_with_JSON_LD_11(jsonld_r4_dir, jsonld_output_dir);
            generator.generate_ttl_with_JSON_LD(jsonld_r4_dir + "/" + jsonld_output_dir, ttl_output_dir);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
