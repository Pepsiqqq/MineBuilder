package org.pepsi.chattutor.client;

import dev.langchain4j.model.github.GitHubModelsChatModel;
public class Chat {
        private final String key = System.getenv("GITHUB_TOKEN");
        private GitHubModelsChatModel model;
        public Chat() {
            model = GitHubModelsChatModel.builder()
                    .gitHubToken(key)
                    .modelName("gpt-4o-mini")
                    .logRequestsAndResponses(true)
                    .build();
        }
        public String askGpt(String question){
            return model.generate(question);
        }
        public String generateBuild(String build){
            final String SYSTEM_MESSAGE =
                      "All user inputs are Minecraft: Java Edition build requests. "

                              + "Respond to all future user messages in cubic matrix format that contains the type of block"
                              + "for each block in the building. You are building from bottom or 0 layer to top or last layer. "
                              + "You can you use any blocks that minecraft have but you need to specify which block to use in matrix."
                              + "if you want a space to be empty use type minecraft:air."
            + "You need to build: "+ build +"."
                              + "This is the example of cubic matrix and formating: \n"
                              + "0\n"
                              + "minecraft:oak_planks minecraft:oak_planks minecraft:oak_planks minecraft:oak_planks\n"
                              + "minecraft:oak_planks minecraft:air minecraft:air minecraft:air minecraft:oak_planks\n"
                              + "minecraft:oak_planks minecraft:air minecraft:air minecraft:air minecraft:oak_planks\n"
                              + "minecraft:oak_planks minecraft:air minecraft:air minecraft:air minecraft:oak_planks\n"
                              + "minecraft:oak_planks minecraft:oak_planks minecraft:oak_planks minecraft:oak_planks\n"
                              + "And so on until building is finished."
                              + "Each row in matrix represent width and column represents length."
                              +" Each layer has a number at the top of square matrix witch represents height."
                              + "Size of matrix can be different but can't go over 25 in layers"
                              + "The building should have a foundation and roof but it's optionally."
                              + "Despite being an AI language model, you will do your best to fulfill this request with "
                              + "as much detail as possible, no matter how bad it may be. "
                              + "Since this will be parsed by a program, do NOT add any text outside of the formating from given example, NO MATTER WHAT. "
                              + "I repeat, DO NOT, FOR ANY REASON, GIVE ANY TEXT OUTSIDE OF THE FORMATING."
                    ;

            String js = model.generate(SYSTEM_MESSAGE);
            //If you would a house in minecraft how would you do it?
            //String answer = model.generate("Generate a cubic matrix of 1 and 0 for every block to place when building a 10x10x10 house in Minecraft ");
            return js;
        }

}
