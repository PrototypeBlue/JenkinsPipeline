


def call(){
    properties(
        //Parameters to asign, like the number of artifacts
    )

    def stages          = new Stages()
    def pipelinesStages

    pipelinesStages = [
        'buildCodigo'
    ]
    def invokedStages       = getInvokedStages(pipelineStages, params.soloStage)

    invokedStages.each{ stageName ->
        stage(stageName){
            try {
                figlet stageName
                (genericStages.isGenericStage(stageName)) ? genericStages."${stageName}"() : stages."${stageName}"()
            } catch(Exception ex) {
                throw new Exception(output.variablesPostFailure(ex))
            }
        }
    }

/**
* Verifica que los stages invocados por parámetros existan dentro de su pipeline.
*
* @param pipelineStages array que contiene los stages definidos en el pipeline.
* @param soloStage array que contiene los stages a invocar en dicho pipeline.
* @return array 
*
* Llamada: getInvokedStages(pipelineStages, soloStage)
*/
    def getInvokedStages(pipelineStages, soloStage){
        def invokedStages = []
        if (soloStage?.trim()){
            soloStage.split(';').each{
                if (it in pipelineStages){
                    invokedStages.add(it)
                } else {
                    error("${it} no existe como Stage en este pipeline. Stages disponibles: " + pipelineStages)
                }
            }
            println "Stages a ejecutar: ${invokedStages}"
        } else {
            invokedStages = pipelineStages
            println "Se ejecutarán todos los stages."
        }
        return invokedStages
    }


}