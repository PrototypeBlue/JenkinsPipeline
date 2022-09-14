node{
    stage('input'){
        userInput = input(
            id: 'userInput', message: 'Please, ingress URL to be analyze',
            parameters: [ string(description: 'Example of URL: www.google.com', name: 'URL'),
            choice(
                description: 'Type of URL',
                choices: ['API','DAST'], 
                name: 'type_vbe'
            )
            //file(name: 'Archive', description: 'Zip con archivos a analizar ej: solicitudVBExperto.zip')
            
            ])

        println("URL: "+userInput.URL)
        //println("Archive: "+userInput.Archive)
        no_oc = userInput.URL
        type_vbe=userInput.type_vbe
        //filedata = userInput.Archive

        //pathFile = "${filedata}"
        //File zipFile = new File(pathFile)

        currentBuild.displayName = currentBuild.displayName+" - URL"+no_oc
        env.CURRENTB = currentBuild.number.toString()

        
    }
    
    stage('parseUrl'){
    dir (env.CURRENTB) {
        writeFile file:'dast', text:''
    }
        echo '=========== Analysis begins =============='
        def analysisVars = getNameOfFile(URL,type_vbe)
    }
}

def getNameOfFile(URL,type_vbe){
        Date date = new Date()
        String datePart = date.format("ddMMyyyyHHmmss")
        env.REPORTNAME = datePart+'-DAST'
        if(type_vbe == 'DAST'){
            bat "docker run -v %cd%:/zap/wrk/:rw -t owasp/zap2docker-weekly zap-api-scan.py -i -a -S -T 20 -t https://pub.dev -f openapi -J ${CURRENTB}/${REPORTNAME}.json -r ${CURRENTB}/${REPORTNAME}.html active-scan -deamon "
        }
            archiveArtifacts artifacts: "${currentBuild.number.toString()}/${REPORTNAME}.html"

}