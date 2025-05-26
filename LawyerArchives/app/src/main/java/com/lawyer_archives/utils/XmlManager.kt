package com.lawyer_archives.utils

import android.content.Context
import com.lawyer_archives.models.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import org.xmlpull.v1.XmlSerializer
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream // Added from XmlManager2.kt.txt 

object XmlManager {

    private const val CASES_FILE = "cases.xml"
    private const val CLIENTS_FILE = "clients.xml"
    private const val SESSIONS_FILE = "sessions.xml"
    private const val MEETINGS_FILE = "meetings.xml"
    private const val DOCUMENTS_FILE = "documents.xml" // Added from XmlManager2.kt.txt 

    // --- Cases ---
    fun saveCases(context: Context, cases: List<Case>) {
        try {
            val file = File(context.filesDir, CASES_FILE)
            val fos = FileOutputStream(file) [cite: 21]

            val serializer = XmlPullParserFactory.newInstance().newSerializer()
            serializer.setOutput(fos, "UTF-8") [cite: 21]
            serializer.startDocument("UTF-8", true) [cite: 21]
            serializer.startTag("", "Cases") [cite: 21]

            for (item in cases) {
                serializer.startTag("", "Case") [cite: 22]
                serializer.attribute("", "id", item.id) [cite: 22]

                serializer.startTag("", "Title") [cite: 22]
                serializer.text(item.title) [cite: 22]
                serializer.endTag("", "Title") [cite: 22]

                serializer.startTag("", "Description") [cite: 22]
                serializer.text(item.description) [cite: 23]
                serializer.endTag("", "Description") [cite: 23]

                serializer.startTag("", "ClientName") [cite: 23]
                serializer.text(item.clientName) [cite: 23]
                serializer.endTag("", "ClientName") [cite: 23]

                serializer.startTag("", "CourtDate") [cite: 23]
                serializer.text(item.courtDate) [cite: 24]
                serializer.endTag("", "CourtDate") [cite: 24]

                serializer.startTag("", "Status") [cite: 24]
                serializer.text(item.status) [cite: 24]
                serializer.endTag("", "Status") [cite: 24]

                serializer.startTag("", "AddedDate") [cite: 24]
                serializer.text(item.addedDate) [cite: 25]
                serializer.endTag("", "AddedDate") [cite: 25]

                serializer.endTag("", "Case") [cite: 25]
            }

            serializer.endTag("", "Cases") [cite: 25]
            serializer.endDocument() [cite: 25]
            serializer.flush() [cite: 25]
            fos.close() [cite: 25]
        } catch (e: Exception) {
            e.printStackTrace() [cite: 26]
        }
    }

    fun loadCases(context: Context): MutableList<Case> {
        val cases = mutableListOf<Case>()
        try {
            val file = File(context.filesDir, CASES_FILE)
            if (!file.exists()) return cases [cite: 26]

            val fis = FileInputStream(file) [cite: 27]
            val factory = XmlPullParserFactory.newInstance() [cite: 27]
            val parser = factory.newPullParser() [cite: 27]
            parser.setInput(fis, "UTF-8") [cite: 27]

            var eventType = parser.eventType [cite: 28]
            var currentCase: Case? = null [cite: 28]

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        val name = parser.name [cite: 29]
                        if (name == "Case") {
                            val id = parser.getAttributeValue(null, "id") [cite: 29]
                            currentCase = Case(id = id ?: "") [cite: 29]
                        } else {
                            when (name) {
                                "Title" -> currentCase?.title = parser.nextText() [cite: 30]
                                "Description" -> currentCase?.description = parser.nextText() [cite: 31]
                                "ClientName" -> currentCase?.clientName = parser.nextText() [cite: 31]
                                "CourtDate" -> currentCase?.courtDate = parser.nextText() [cite: 31]
                                "Status" -> currentCase?.status = parser.nextText() [cite: 32]
                                "AddedDate" -> currentCase?.addedDate = parser.nextText() [cite: 32]
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "Case") {
                            currentCase?.let { cases.add(it) } [cite: 34]
                        }
                    }
                }
                eventType = parser.next() [cite: 34]
            }
            fis.close() [cite: 35]
        } catch (e: Exception) {
            e.printStackTrace() [cite: 35]
        }
        return cases
    }

    // --- Clients ---
    fun saveClients(context: Context, clients: List<Client>) {
        try {
            val file = File(context.filesDir, CLIENTS_FILE)
            val fos = FileOutputStream(file) [cite: 36]

            val serializer = XmlPullParserFactory.newInstance().newSerializer()
            serializer.setOutput(fos, "UTF-8") [cite: 36]
            serializer.startDocument("UTF-8", true) [cite: 36]
            serializer.startTag("", "Clients") [cite: 36]

            for (item in clients) {
                serializer.startTag("", "Client") [cite: 37]
                serializer.attribute("", "id", item.id) [cite: 37]

                serializer.startTag("", "Name") [cite: 37]
                serializer.text(item.name) [cite: 37]
                serializer.endTag("", "Name") [cite: 37]

                serializer.startTag("", "Phone") [cite: 37]
                serializer.text(item.phone) [cite: 38]
                serializer.endTag("", "Phone") [cite: 38]

                serializer.startTag("", "Email") [cite: 38]
                serializer.text(item.email) [cite: 38]
                serializer.endTag("", "Email") [cite: 38]

                serializer.startTag("", "Address") [cite: 38]
                serializer.text(item.address) [cite: 39]
                serializer.endTag("", "Address") [cite: 39]

                serializer.startTag("", "CaseCount") [cite: 39]
                serializer.text(item.caseCount.toString()) [cite: 39]
                serializer.endTag("", "CaseCount") [cite: 39]

                serializer.endTag("", "Client") [cite: 39]
            }

            serializer.endTag("", "Clients") [cite: 40]
            serializer.endDocument() [cite: 40]
            serializer.flush() [cite: 40]
            fos.close() [cite: 40]
        } catch (e: Exception) {
            e.printStackTrace() [cite: 40]
        }
    }

    fun loadClients(context: Context): MutableList<Client> {
        val clients = mutableListOf<Client>() [cite: 41]
        try {
            val file = File(context.filesDir, CLIENTS_FILE)
            if (!file.exists()) return clients [cite: 41]

            val fis = FileInputStream(file) [cite: 41]
            val factory = XmlPullParserFactory.newInstance() [cite: 41]
            val parser = factory.newPullParser() [cite: 41]
            parser.setInput(fis, "UTF-8") [cite: 41]

            var eventType = parser.eventType [cite: 42]
            var currentClient: Client? = null [cite: 43]

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        val name = parser.name [cite: 44]
                        if (name == "Client") {
                            val id = parser.getAttributeValue(null, "id") [cite: 44]
                            currentClient = Client(id = id ?: "") [cite: 44]
                        } else {
                            when (name) {
                                "Name" -> currentClient?.name = parser.nextText() [cite: 45]
                                "Phone" -> currentClient?.phone = parser.nextText() [cite: 46]
                                "Email" -> currentClient?.email = parser.nextText() [cite: 46]
                                "Address" -> currentClient?.address = parser.nextText() [cite: 46]
                                "CaseCount" -> currentClient?.caseCount = parser.nextText().toIntOrNull() ?: 0 [cite: 47]
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "Client") {
                            currentClient?.let { clients.add(it) } [cite: 48]
                        }
                    }
                }
                eventType = parser.next() [cite: 49]
            }
            fis.close() [cite: 49]
        } catch (e: Exception) {
            e.printStackTrace() [cite: 50]
        }
        return clients
    }

    // --- Court Sessions (Name changed from Sessions to CourtSessions for clarity, as per XmlManager2.kt.txt) ---
    fun saveCourtSessions(context: Context, sessions: List<CourtSession>) { // Name changed 
        try {
            val file = File(context.filesDir, SESSIONS_FILE)
            val fos = FileOutputStream(file) [cite: 50]

            val serializer = XmlPullParserFactory.newInstance().newSerializer()
            serializer.setOutput(fos, "UTF-8") [cite: 51]
            serializer.startDocument("UTF-8", true) [cite: 51]
            serializer.startTag("", "Sessions") [cite: 51]

            for (item in sessions) {
                serializer.startTag("", "Session") [cite: 51]
                serializer.attribute("", "id", item.id) [cite: 51]

                serializer.startTag("", "CaseId") [cite: 51]
                serializer.text(item.caseId) [cite: 52]
                serializer.endTag("", "CaseId") [cite: 52]

                serializer.startTag("", "CaseTitle") [cite: 52]
                serializer.text(item.caseTitle) [cite: 52]
                serializer.endTag("", "CaseTitle") [cite: 52]

                serializer.startTag("", "SessionDate") [cite: 52]
                serializer.text(item.sessionDate) [cite: 53]
                serializer.endTag("", "SessionDate") [cite: 53]

                serializer.startTag("", "Location") [cite: 53]
                serializer.text(item.location) [cite: 53]
                serializer.endTag("", "Location") [cite: 53]

                serializer.startTag("", "Description") [cite: 53]
                serializer.text(item.description) [cite: 54]
                serializer.endTag("", "Description") [cite: 54]

                serializer.startTag("", "IsCompleted") [cite: 54]
                serializer.text(item.isCompleted.toString()) [cite: 54]
                serializer.endTag("", "IsCompleted") [cite: 54]

                serializer.endTag("", "Session") [cite: 54]
            }

            serializer.endTag("", "Sessions") [cite: 55]
            serializer.endDocument() [cite: 55]
            serializer.flush() [cite: 55]
            fos.close() [cite: 55]
        } catch (e: Exception) {
            e.printStackTrace() [cite: 55]
        }
    }

    fun loadCourtSessions(context: Context): MutableList<CourtSession> { // Name changed 
        val sessions = mutableListOf<CourtSession>() [cite: 56]
        try {
            val file = File(context.filesDir, SESSIONS_FILE)
            if (!file.exists()) return sessions [cite: 56]

            val fis = FileInputStream(file) [cite: 56]
            val factory = XmlPullParserFactory.newInstance() [cite: 56]
            val parser = factory.newPullParser() [cite: 57]
            parser.setInput(fis, "UTF-8") [cite: 57]

            var eventType = parser.eventType [cite: 57]
            var currentSession: CourtSession? = null [cite: 58]

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        val name = parser.name [cite: 59]
                        if (name == "Session") {
                            val id = parser.getAttributeValue(null, "id") [cite: 59]
                            currentSession = CourtSession(id = id ?: "") [cite: 59]
                        } else {
                            when (name) {
                                "CaseId" -> currentSession?.caseId = parser.nextText() [cite: 60]
                                "CaseTitle" -> currentSession?.caseTitle = parser.nextText() [cite: 61]
                                "SessionDate" -> currentSession?.sessionDate = parser.nextText() [cite: 61]
                                "Location" -> currentSession?.location = parser.nextText() [cite: 61]
                                "Description" -> currentSession?.description = parser.nextText() [cite: 62]
                                "IsCompleted" -> currentSession?.isCompleted = parser.nextText().toBoolean() [cite: 62]
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "Session") {
                            currentSession?.let { sessions.add(it) } [cite: 64]
                        }
                    }
                }
                eventType = parser.next() [cite: 64]
            }
            fis.close() [cite: 65]
        } catch (e: Exception) {
            e.printStackTrace() [cite: 65]
        }
        return sessions
    }

    // --- Meetings ---
    fun saveMeetings(context: Context, meetings: List<Meeting>) {
        try {
            val file = File(context.filesDir, MEETINGS_FILE)
            val fos = FileOutputStream(file) [cite: 66]

            val serializer = XmlPullParserFactory.newInstance().newSerializer()
            serializer.setOutput(fos, "UTF-8") [cite: 66]
            serializer.startDocument("UTF-8", true) [cite: 66]
            serializer.startTag("", "Meetings") [cite: 66]

            for (item in meetings) {
                serializer.startTag("", "Meeting") [cite: 67]
                serializer.attribute("", "id", item.id) [cite: 67]

                serializer.startTag("", "ClientId") [cite: 67]
                serializer.text(item.clientId) [cite: 67]
                serializer.endTag("", "ClientId") [cite: 67]

                serializer.startTag("", "ClientName") [cite: 67]
                serializer.text(item.clientName) [cite: 68]
                serializer.endTag("", "ClientName") [cite: 68]

                serializer.startTag("", "MeetingDate") [cite: 68]
                serializer.text(item.meetingDate) [cite: 68]
                serializer.endTag("", "MeetingDate") [cite: 68]

                serializer.startTag("", "Topic") [cite: 68]
                serializer.text(item.topic) [cite: 69]
                serializer.endTag("", "Topic") [cite: 69]

                serializer.startTag("", "Duration") [cite: 69]
                serializer.text(item.duration) [cite: 69]
                serializer.endTag("", "Duration") [cite: 69]

                serializer.startTag("", "Location") [cite: 69]
                serializer.text(item.location) [cite: 70]
                serializer.endTag("", "Location") [cite: 70]

                serializer.endTag("", "Meeting") [cite: 70]
            }

            serializer.endTag("", "Meetings") [cite: 70]
            serializer.endDocument() [cite: 70]
            serializer.flush() [cite: 70]
            fos.close() [cite: 70]
        } catch (e: Exception) {
            e.printStackTrace() [cite: 71]
        }
    }

    fun loadMeetings(context: Context): MutableList<Meeting> {
        val meetings = mutableListOf<Meeting>() [cite: 71]
        try {
            val file = File(context.filesDir, MEETINGS_FILE)
            if (!file.exists()) return meetings [cite: 71]

            val fis = FileInputStream(file) [cite: 72]
            val factory = XmlPullParserFactory.newInstance() [cite: 72]
            val parser = factory.newPullParser() [cite: 72]
            parser.setInput(fis, "UTF-8") [cite: 72]

            var eventType = parser.eventType [cite: 73]
            var currentMeeting: Meeting? = null [cite: 73]

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        val name = parser.name [cite: 74]
                        if (name == "Meeting") {
                            val id = parser.getAttributeValue(null, "id") [cite: 74]
                            currentMeeting = Meeting(id = id ?: "") [cite: 74]
                        } else {
                            when (name) {
                                "ClientId" -> currentMeeting?.clientId = parser.nextText() [cite: 75]
                                "ClientName" -> currentMeeting?.clientName = parser.nextText() [cite: 76]
                                "MeetingDate" -> currentMeeting?.meetingDate = parser.nextText() [cite: 76]
                                "Topic" -> currentMeeting?.topic = parser.nextText() [cite: 76]
                                "Duration" -> currentMeeting?.duration = parser.nextText() [cite: 77]
                                "Location" -> currentMeeting?.location = parser.nextText() [cite: 77]
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "Meeting") {
                            currentMeeting?.let { meetings.add(it) } [cite: 79]
                        }
                    }
                }
                eventType = parser.next() [cite: 79]
            }
            fis.close() [cite: 80]
        } catch (e: Exception) {
            e.printStackTrace() [cite: 80]
        }
        return meetings
    }

    // --- Documents (NEW METHODS - Added from XmlManager2.kt.txt) ---
    fun saveDocuments(context: Context, documents: List<Document>) {
        try {
            val file = File(context.filesDir, DOCUMENTS_FILE) [cite: 4]
            val fos = FileOutputStream(file) [cite: 4]

            val serializer = XmlPullParserFactory.newInstance().newSerializer() [cite: 4]
            serializer.setOutput(fos, "UTF-8") [cite: 5]
            serializer.startDocument("UTF-8", true) [cite: 5]
            serializer.startTag("", "Documents") [cite: 5]

            for (item in documents) {
                serializer.startTag("", "Document") [cite: 5]
                serializer.attribute("", "id", item.id) [cite: 5]

                serializer.startTag("", "CaseId") [cite: 5]
                serializer.text(item.caseId) [cite: 6]
                serializer.endTag("", "CaseId") [cite: 6]

                serializer.startTag("", "Title") [cite: 6]
                serializer.text(item.title) [cite: 6]
                serializer.endTag("", "Title") [cite: 6]

                serializer.startTag("", "FilePath") [cite: 7]
                serializer.text(item.filePath) [cite: 7]
                serializer.endTag("", "FilePath") [cite: 7]

                serializer.startTag("", "FileExtension") [cite: 7]
                serializer.text(item.fileExtension) [cite: 7]
                serializer.endTag("", "FileExtension") [cite: 7]

                serializer.startTag("", "AddedDate") [cite: 7]
                serializer.text(item.addedDate) [cite: 8]
                serializer.endTag("", "AddedDate") [cite: 8]

                serializer.endTag("", "Document") [cite: 8]
            }
            serializer.endTag("", "Documents") [cite: 8]
            serializer.endDocument() [cite: 8]
            fos.close() [cite: 8]
        } catch (e: Exception) {
            e.printStackTrace() [cite: 9]
        }
    }

    fun loadDocuments(context: Context): MutableList<Document> {
        val documents = mutableListOf<Document>() [cite: 9]
        try {
            val file = File(context.filesDir, DOCUMENTS_FILE) [cite: 9]
            if (!file.exists()) {
                return documents // Return empty list if file doesn't exist 
            }
            val fis = FileInputStream(file) [cite: 10]
            val parserFactory = XmlPullParserFactory.newInstance() [cite: 10]
            val parser = parserFactory.newPullParser() [cite: 10]
            parser.setInput(fis, null) [cite: 10]

            var eventType = parser.eventType [cite: 10]
            var currentDocument: Document? = null [cite: 11, 12]

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        val name = parser.name [cite: 12]
                        if (name == "Document") { [cite: 13]
                            val id = parser.getAttributeValue(null, "id") [cite: 13]
                            currentDocument = Document(id = id ?: "") [cite: 13]
                        } else {
                            when (name) {
                                "CaseId" -> currentDocument?.caseId = parser.nextText() [cite: 14]
                                "Title" -> currentDocument?.title = parser.nextText() [cite: 15]
                                "FilePath" -> currentDocument?.filePath = parser.nextText() [cite: 15]
                                "FileExtension" -> currentDocument?.fileExtension = parser.nextText() [cite: 15]
                                "AddedDate" -> currentDocument?.addedDate = parser.nextText() [cite: 16]
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> { [cite: 17]
                        if (parser.name == "Document") { [cite: 17]
                            currentDocument?.let { documents.add(it) } [cite: 17]
                        }
                    }
                }
                eventType = parser.next() [cite: 18]
            }
            fis.close() [cite: 18]
        } catch (e: Exception) {
            e.printStackTrace() [cite: 19]
        }
        return documents
    }
}