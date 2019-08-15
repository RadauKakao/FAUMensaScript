import java.time.Instant

// Calls method with two stated arguments (Mensa, Date)
if (args.length == 2) {
    mensaBot(args[0], args[1])
}
    // If no date is stated take today's date
    else if (args.length == 1) {
     def date = new Date().format('ddMMyyyy')
     mensaBot(args[0], date)
    }

    // If no argument was given return Error
    else if (args.length == 0) {
    println "Error! No location was given."
    return
    }

def mensaBot(def mensa, def date) {

    // URL to read
    def url = "https://www.max-manager.de/daten-extern/sw-erlangen-nuernberg/xml/mensa-${mensa}.xml"

    // Parse the xml text
    def doc = new XmlSlurper().parse(url)

    // If no date is stated return error and exit
    if (date != null) {
        date = Date.parse('ddMMyyyy', "$date").format('ddMMyyyy')
    }
    else {
        println "Error! Found no valid date."
        return
    }

    def i = 0                                            // Define counter to catch needed iteration in xml for stated date
    boolean foundToday                                  // Define bool for loop until the stated date is matched

    // Search for stated date's plan
    // Iterates through the timeStamps until it matches stated date and saves the iteration in counter $i
    while (!foundToday) {


        def timeStamp = doc.tag[i]['@timestamp'].toString() as Integer  // Formats timestamp from xml to integer
        def formatedTimeStamp = Date.from(Instant.ofEpochSecond(timeStamp)).format('ddMMyyyy')  // Formats the timestamp to ddMMyyyy

        // Debug stuff for comparing dates
            //println date
            //println formatedTimeStamp

        // Checks if date (ddMMyyyy) matches xml timestamp (ddMMyyyy)
        if (date == formatedTimeStamp) {
            foundToday = true
        } else {
            i++
        }
    }
    // Call method with the parsed xml and iterator i
    returnSearchedMeal(doc, i)
}

// Prints all meals for a day with prices by using the iterator of stated date's date
def returnSearchedMeal(def doc, def i) {
    doc.tag[i].item.each {
        node ->
            println "${node.category}\n${node.title}\n${node.beilagen}\n${node.preis1}€ (Studierende)\n${node.preis2}€ (Bedienstete)\n${node.preis3}€ (Gäste)\n" //the actual thing the script returns
    }
}