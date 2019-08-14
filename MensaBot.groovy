import java.time.Instant

//calls method with stated arguments (Mensa, Date)
mensaBot(args[0], args[1])

def mensaBot(def mensa, def date) {

    // URL to read
    def url = "https://www.max-manager.de/daten-extern/sw-erlangen-nuernberg/xml/mensa-${mensa}.xml"

    // Parse the text
    def doc = new XmlSlurper().parse(url)

    // If no date is stated take today's date
    if (date != null) {
        date = Date.parse('ddMMyyyy', "$date").format('ddMMyyyy')
    }

    if (date == null) {
        date = new Date().format('ddMMyyyy')   //define today's Date
    }

    def i = 0                                            //define counter
    boolean foundToday                                  //define bool for loop

    // Search for stated date's plan
    // Iterates through the timeStamps until it matches stated date and saves the iteration in counter $i
    while (!foundToday) {


        def timeStamp = doc.tag[i]['@timestamp'].toString() as Integer  //formats timestamp from xml to integer
        def formatedTimeStamp = Date.from(Instant.ofEpochSecond(timeStamp)).format('ddMMyyyy')  //formats the timestamp to ddMMyyyy

        //println date
        //println formatedTimeStamp

        //Checks if date (ddMMyyyy) matches xml timestamp (ddMMyyyy)
        if (date == formatedTimeStamp) {
            foundToday = true
        } else {
            i++
        }
    }
    //call the method with xml and iteraton
    searchedMeal(doc, i)

}

//prints all meals for a day with prices by using the iterator of stated date's date
def searchedMeal(def doc, def i) {
    doc.tag[i].item.each {
        node ->
            println "${node.category}\n${node.title}\n${node.beilagen}\n${node.preis1}€ (Studierende)\n${node.preis2}€ (Bedienstete)\n${node.preis2}€ (Gäste)\n"
    }
}