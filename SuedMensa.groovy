import java.time.Instant

// URL to read
def url= "https://www.max-manager.de/daten-extern/sw-erlangen-nuernberg/xml/mensa-sued.xml"

// Parse the text
def doc = new XmlSlurper().parse(url)

def today = new Date().format('ddMMyyyy')   //define today's Date
def i=0                                             //define counter
boolean foundToday                                  //define bool for loop

//Search for today's plan
//Iterates through the timeStamps until it matches today's date and saves the iteration in counter $i
while (!foundToday) {

    def timeStamp = doc.tag[i]['@timestamp'].toString() as Integer                                  //formats timestamp from xml to integer
    def formatedTimeStamp = Date.from(Instant.ofEpochSecond(timeStamp)).format('ddMMyyyy')  //formats the timestamp to ddMMyyyy

    //println today
    //println formatedTimeStamp

    //Checks if today (ddMMyyyy) matches xml timestamp (ddMMyyyy)
    if (today == formatedTimeStamp) {
        foundToday = true
    }
    else {
        i++
    }
}

//prints all meals for a day with prices by using the iterator of today's date
def mealToday(def doc, def i) {
    doc.tag[i].item.each {
        node ->
            println "${node.category}\n${node.title}\n${node.beilagen}\n${node.preis1}€ (Studierende)\n${node.preis2}€ (Bedienstete)\n${node.preis2}€ (Gäste)\n"
    }
}

//call the method with xml and iterator
mealToday(doc, i)
