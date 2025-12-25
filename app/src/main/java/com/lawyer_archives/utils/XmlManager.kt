package com.lawyer_archives.utils

import android.content.Context
import com.lawyer_archives.models.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.*

object XmlManager {

    fun saveCase(context: Context, case: Case): Boolean {
        val cases = loadCases(context).toMutableList()
        cases.add(case)
        return saveCases(context, cases)
    }

    fun loadCases(context: Context): List<Case> {
        return try {
            val file = File(context.filesDir, "cases.xml")
            if (!file.exists()) return emptyList()
            
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(FileInputStream(file), "UTF-8")
            
            val cases = mutableListOf<Case>()
            var eventType = parser.eventType
            
            var id = ""
            var title = ""
            var formationDate = ""
            var clientName = ""
            var clientRole = ""
            var caseSubject = ""
            var status = ""
            var process = ""
            var caseNumber = ""
            var archiveNumber = ""
            var cityJudiciary = ""
            var courtLevelAndType = ""
            var opponentInfo = ""
            var powerOfAttorneyNumber = ""
            var addedDate = ""
            var courtDate = ""
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "case" -> {
                                id = ""
                                title = ""
                                formationDate = ""
                                clientName = ""
                                clientRole = ""
                                caseSubject = ""
                                status = ""
                                process = ""
                                caseNumber = ""
                                archiveNumber = ""
                                cityJudiciary = ""
                                courtLevelAndType = ""
                                opponentInfo = ""
                                powerOfAttorneyNumber = ""
                                addedDate = ""
                                courtDate = ""
                            }
                            "id" -> id = parser.nextText()
                            "title" -> title = parser.nextText()
                            "formationDate" -> formationDate = parser.nextText()
                            "clientName" -> clientName = parser.nextText()
                            "clientRole" -> clientRole = parser.nextText()
                            "caseSubject" -> caseSubject = parser.nextText()
                            "status" -> status = parser.nextText()
                            "process" -> process = parser.nextText()
                            "caseNumber" -> caseNumber = parser.nextText()
                            "archiveNumber" -> archiveNumber = parser.nextText()
                            "cityJudiciary" -> cityJudiciary = parser.nextText()
                            "courtLevelAndType" -> courtLevelAndType = parser.nextText()
                            "opponentInfo" -> opponentInfo = parser.nextText()
                            "powerOfAttorneyNumber" -> powerOfAttorneyNumber = parser.nextText()
                            "addedDate" -> addedDate = parser.nextText()
                            "courtDate" -> courtDate = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "case") {
                            val currentCase = Case(
                                id = id,
                                title = title,
                                formationDate = formationDate,
                                clientName = clientName,
                                clientRole = clientRole,
                                caseSubject = caseSubject,
                                status = status,
                                process = process,
                                caseNumber = caseNumber,
                                archiveNumber = archiveNumber,
                                cityJudiciary = cityJudiciary,
                                courtLevelAndType = courtLevelAndType,
                                opponentInfo = opponentInfo,
                                powerOfAttorneyNumber = powerOfAttorneyNumber,
                                addedDate = addedDate,
                                courtDate = courtDate
                            )
                            cases.add(currentCase)
                        }
                    }
                }
                eventType = parser.next()
            }
            cases
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveCases(context: Context, cases: List<Case>): Boolean {
        return try {
            val file = File(context.filesDir, "cases.xml")
            val writer = FileWriter(file)
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            writer.write("<cases>\n")
            cases.forEach { case ->
                writer.write("  <case>\n")
                writer.write("    <id>${escapeXml(case.id)}</id>\n")
                writer.write("    <title>${escapeXml(case.title)}</title>\n")
                writer.write("    <formationDate>${escapeXml(case.formationDate)}</formationDate>\n")
                writer.write("    <clientName>${escapeXml(case.clientName)}</clientName>\n")
                writer.write("    <clientRole>${escapeXml(case.clientRole)}</clientRole>\n")
                writer.write("    <caseSubject>${escapeXml(case.caseSubject)}</caseSubject>\n")
                writer.write("    <status>${escapeXml(case.status)}</status>\n")
                writer.write("    <process>${escapeXml(case.process)}</process>\n")
                writer.write("    <caseNumber>${escapeXml(case.caseNumber)}</caseNumber>\n")
                writer.write("    <archiveNumber>${escapeXml(case.archiveNumber)}</archiveNumber>\n")
                writer.write("    <cityJudiciary>${escapeXml(case.cityJudiciary)}</cityJudiciary>\n")
                writer.write("    <courtLevelAndType>${escapeXml(case.courtLevelAndType)}</courtLevelAndType>\n")
                writer.write("    <opponentInfo>${escapeXml(case.opponentInfo)}</opponentInfo>\n")
                writer.write("    <powerOfAttorneyNumber>${escapeXml(case.powerOfAttorneyNumber)}</powerOfAttorneyNumber>\n")
                writer.write("    <addedDate>${escapeXml(case.addedDate)}</addedDate>\n")
                writer.write("    <courtDate>${escapeXml(case.courtDate)}</courtDate>\n")
                writer.write("  </case>\n")
            }
            writer.write("</cases>")
            writer.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun saveSessions(context: Context, sessions: List<CourtSession>): Boolean {
        return try {
            val file = File(context.filesDir, "sessions.xml")
            val writer = FileWriter(file)
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            writer.write("<sessions>\n")
            sessions.forEach { session ->
                writer.write("  <session>\n")
                writer.write("    <id>${escapeXml(session.id)}</id>\n")
                writer.write("    <title>${escapeXml(session.title)}</title>\n")
                writer.write("    <description>${escapeXml(session.description)}</description>\n")
                writer.write("    <clientName>${escapeXml(session.clientName)}</clientName>\n")
                writer.write("    <courtDate>${escapeXml(session.courtDate)}</courtDate>\n")
                writer.write("    <courtTime>${escapeXml(session.courtTime)}</courtTime>\n")
                writer.write("    <courtBranch>${escapeXml(session.courtBranch)}</courtBranch>\n")
                writer.write("    <status>${escapeXml(session.status)}</status>\n")
                writer.write("    <addedDate>${escapeXml(session.addedDate)}</addedDate>\n")
                writer.write("  </session>\n")
            }
            writer.write("</sessions>")
            writer.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun loadSessions(context: Context): List<CourtSession> {
        return try {
            val file = File(context.filesDir, "sessions.xml")
            if (!file.exists()) return emptyList()
            
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(FileInputStream(file), "UTF-8")
            
            val sessions = mutableListOf<CourtSession>()
            var eventType = parser.eventType
            
            var id = ""
            var title = ""
            var description = ""
            var clientName = ""
            var courtDate = ""
            var courtTime = ""
            var courtBranch = ""
            var status = ""
            var addedDate = ""
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "session" -> {
                                id = ""
                                title = ""
                                description = ""
                                clientName = ""
                                courtDate = ""
                                courtTime = ""
                                courtBranch = ""
                                status = ""
                                addedDate = ""
                            }
                            "id" -> id = parser.nextText()
                            "title" -> title = parser.nextText()
                            "description" -> description = parser.nextText()
                            "clientName" -> clientName = parser.nextText()
                            "courtDate" -> courtDate = parser.nextText()
                            "courtTime" -> courtTime = parser.nextText()
                            "courtBranch" -> courtBranch = parser.nextText()
                            "status" -> status = parser.nextText()
                            "addedDate" -> addedDate = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "session") {
                            sessions.add(CourtSession(
                                id = id,
                                title = title,
                                description = description,
                                clientName = clientName,
                                courtDate = courtDate,
                                courtTime = courtTime,
                                courtBranch = courtBranch,
                                status = status,
                                addedDate = addedDate
                            ))
                        }
                    }
                }
                eventType = parser.next()
            }
            sessions
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveMeetings(context: Context, meetings: List<Meeting>): Boolean {
        return try {
            val file = File(context.filesDir, "meetings.xml")
            val writer = FileWriter(file)
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            writer.write("<meetings>\n")
            meetings.forEach { meeting ->
                writer.write("  <meeting>\n")
                writer.write("    <id>${escapeXml(meeting.id)}</id>\n")
                writer.write("    <title>${escapeXml(meeting.title)}</title>\n")
                writer.write("    <clientName>${escapeXml(meeting.clientName)}</clientName>\n")
                writer.write("    <description>${escapeXml(meeting.description)}</description>\n")
                writer.write("    <date>${escapeXml(meeting.date)}</date>\n")
                writer.write("    <time>${escapeXml(meeting.time)}</time>\n")
                writer.write("    <addedDate>${escapeXml(meeting.addedDate)}</addedDate>\n")
                writer.write("  </meeting>\n")
            }
            writer.write("</meetings>")
            writer.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun loadMeetings(context: Context): List<Meeting> {
        return try {
            val file = File(context.filesDir, "meetings.xml")
            if (!file.exists()) return emptyList()
            
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(FileInputStream(file), "UTF-8")
            
            val meetings = mutableListOf<Meeting>()
            var eventType = parser.eventType
            
            var id = ""
            var title = ""
            var clientName = ""
            var description = ""
            var date = ""
            var time = ""
            var addedDate = ""
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "meeting" -> {
                                id = ""
                                title = ""
                                clientName = ""
                                description = ""
                                date = ""
                                time = ""
                                addedDate = ""
                            }
                            "id" -> id = parser.nextText()
                            "title" -> title = parser.nextText()
                            "clientName" -> clientName = parser.nextText()
                            "description" -> description = parser.nextText()
                            "date" -> date = parser.nextText()
                            "time" -> time = parser.nextText()
                            "addedDate" -> addedDate = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "meeting") {
                            meetings.add(Meeting(
                                id = id,
                                title = title,
                                clientName = clientName,
                                description = description,
                                date = date,
                                time = time,
                                addedDate = addedDate
                            ))
                        }
                    }
                }
                eventType = parser.next()
            }
            meetings
        } catch (e: Exception) {
            emptyList()
        }
    }

    // ===================== General Meetings =====================
    
    fun saveGeneralMeetings(context: Context, meetings: List<GeneralMeeting>): Boolean {
        return try {
            val file = File(context.filesDir, "general_meetings.xml")
            val writer = FileWriter(file)
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            writer.write("<generalMeetings>\n")
            meetings.forEach { meeting ->
                writer.write("  <meeting>\n")
                writer.write("    <id>${escapeXml(meeting.id)}</id>\n")
                writer.write("    <title>${escapeXml(meeting.title)}</title>\n")
                writer.write("    <date>${escapeXml(meeting.date)}</date>\n")
                writer.write("    <time>${escapeXml(meeting.time)}</time>\n")
                writer.write("    <location>${escapeXml(meeting.location)}</location>\n")
                writer.write("    <description>${escapeXml(meeting.description)}</description>\n")
                writer.write("    <addedDate>${escapeXml(meeting.addedDate)}</addedDate>\n")
                writer.write("  </meeting>\n")
            }
            writer.write("</generalMeetings>")
            writer.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun loadGeneralMeetings(context: Context): List<GeneralMeeting> {
        return try {
            val file = File(context.filesDir, "general_meetings.xml")
            if (!file.exists()) return emptyList()
            
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(FileInputStream(file), "UTF-8")
            
            val meetings = mutableListOf<GeneralMeeting>()
            var eventType = parser.eventType
            
            var id = ""
            var title = ""
            var date = ""
            var time = ""
            var location = ""
            var description = ""
            var addedDate = ""
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "meeting" -> {
                                id = ""
                                title = ""
                                date = ""
                                time = ""
                                location = ""
                                description = ""
                                addedDate = ""
                            }
                            "id" -> id = parser.nextText()
                            "title" -> title = parser.nextText()
                            "date" -> date = parser.nextText()
                            "time" -> time = parser.nextText()
                            "location" -> location = parser.nextText()
                            "description" -> description = parser.nextText()
                            "addedDate" -> addedDate = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "meeting") {
                            meetings.add(GeneralMeeting(
                                id = id,
                                title = title,
                                date = date,
                                time = time,
                                location = location,
                                description = description,
                                addedDate = addedDate
                            ))
                        }
                    }
                }
                eventType = parser.next()
            }
            meetings
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun saveRealClients(context: Context, clients: List<RealClient>): Boolean {
        return try {
            val file = File(context.filesDir, "real_clients.xml")
            val writer = FileWriter(file)
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            writer.write("<clients>\n")
            clients.forEach { client ->
                writer.write("  <client>\n")
                writer.write("    <id>${escapeXml(client.id)}</id>\n")
                writer.write("    <fullName>${escapeXml(client.fullName)}</fullName>\n")
                writer.write("    <fatherName>${escapeXml(client.fatherName)}</fatherName>\n")
                writer.write("    <idCardNumber>${escapeXml(client.idCardNumber)}</idCardNumber>\n")
                writer.write("    <nationalId>${escapeXml(client.nationalId)}</nationalId>\n")
                writer.write("    <birthDate>${escapeXml(client.birthDate)}</birthDate>\n")
                writer.write("    <birthPlace>${escapeXml(client.birthPlace)}</birthPlace>\n")
                writer.write("    <address>${escapeXml(client.address)}</address>\n")
                writer.write("    <phone>${escapeXml(client.phone)}</phone>\n")
                writer.write("    <phoneNumber>${escapeXml(client.phoneNumber)}</phoneNumber>\n")
                writer.write("    <occupation>${escapeXml(client.occupation)}</occupation>\n")
                writer.write("    <email>${escapeXml(client.email)}</email>\n")
                writer.write("    <postalCode>${escapeXml(client.postalCode)}</postalCode>\n")
                writer.write("    <description>${escapeXml(client.description)}</description>\n")
                writer.write("    <addedDate>${escapeXml(client.addedDate)}</addedDate>\n")
                writer.write("  </client>\n")
            }
            writer.write("</clients>")
            writer.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun loadRealClients(context: Context): List<RealClient> {
        return try {
            val file = File(context.filesDir, "real_clients.xml")
            if (!file.exists()) return emptyList()
            
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(FileInputStream(file), "UTF-8")
            
            val clients = mutableListOf<RealClient>()
            var eventType = parser.eventType
            
            var id = ""
            var fullName = ""
            var fatherName = ""
            var idCardNumber = ""
            var nationalId = ""
            var birthDate = ""
            var birthPlace = ""
            var address = ""
            var phone = ""
            var phoneNumber = ""
            var occupation = ""
            var email = ""
            var postalCode = ""
            var description = ""
            var addedDate = ""
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "client" -> {
                                id = ""
                                fullName = ""
                                fatherName = ""
                                idCardNumber = ""
                                nationalId = ""
                                birthDate = ""
                                birthPlace = ""
                                address = ""
                                phone = ""
                                phoneNumber = ""
                                occupation = ""
                                email = ""
                                postalCode = ""
                                description = ""
                                addedDate = ""
                            }
                            "id" -> id = parser.nextText()
                            "fullName" -> fullName = parser.nextText()
                            "fatherName" -> fatherName = parser.nextText()
                            "idCardNumber" -> idCardNumber = parser.nextText()
                            "nationalId" -> nationalId = parser.nextText()
                            "birthDate" -> birthDate = parser.nextText()
                            "birthPlace" -> birthPlace = parser.nextText()
                            "address" -> address = parser.nextText()
                            "phone" -> phone = parser.nextText()
                            "phoneNumber" -> phoneNumber = parser.nextText()
                            "occupation" -> occupation = parser.nextText()
                            "email" -> email = parser.nextText()
                            "postalCode" -> postalCode = parser.nextText()
                            "description" -> description = parser.nextText()
                            "addedDate" -> addedDate = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "client") {
                            clients.add(RealClient(
                                id = id,
                                fullName = fullName,
                                fatherName = fatherName,
                                idCardNumber = idCardNumber,
                                nationalId = nationalId,
                                birthDate = birthDate,
                                birthPlace = birthPlace,
                                address = address,
                                phone = phone,
                                phoneNumber = phoneNumber,
                                occupation = occupation,
                                email = email,
                                postalCode = postalCode,
                                description = description,
                                addedDate = addedDate
                            ))
                        }
                    }
                }
                eventType = parser.next()
            }
            clients
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveLegalClients(context: Context, clients: List<LegalClient>): Boolean {
        return try {
            val file = File(context.filesDir, "legal_clients.xml")
            val writer = FileWriter(file)
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            writer.write("<clients>\n")
            clients.forEach { client ->
                writer.write("  <client>\n")
                writer.write("    <id>${escapeXml(client.id)}</id>\n")
                writer.write("    <companyName>${escapeXml(client.companyName)}</companyName>\n")
                writer.write("    <nationalId>${escapeXml(client.nationalId)}</nationalId>\n")
                writer.write("    <registrationNumber>${escapeXml(client.registrationNumber)}</registrationNumber>\n")
                writer.write("    <phone>${escapeXml(client.phone)}</phone>\n")
                writer.write("    <address>${escapeXml(client.address)}</address>\n")
                writer.write("    <managerName>${escapeXml(client.managerName)}</managerName>\n")
                writer.write("    <phoneNumber>${escapeXml(client.phoneNumber)}</phoneNumber>\n")
                writer.write("    <email>${escapeXml(client.email)}</email>\n")
                writer.write("    <description>${escapeXml(client.description)}</description>\n")
                writer.write("    <addedDate>${escapeXml(client.addedDate)}</addedDate>\n")
                writer.write("  </client>\n")
            }
            writer.write("</clients>")
            writer.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun loadLegalClients(context: Context): List<LegalClient> {
        return try {
            val file = File(context.filesDir, "legal_clients.xml")
            if (!file.exists()) return emptyList()
            
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(FileInputStream(file), "UTF-8")
            
            val clients = mutableListOf<LegalClient>()
            var eventType = parser.eventType
            
            var id = ""
            var companyName = ""
            var nationalId = ""
            var registrationNumber = ""
            var phone = ""
            var address = ""
            var managerName = ""
            var phoneNumber = ""
            var email = ""
            var description = ""
            var addedDate = ""
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "client" -> {
                                id = ""
                                companyName = ""
                                nationalId = ""
                                registrationNumber = ""
                                phone = ""
                                address = ""
                                managerName = ""
                                phoneNumber = ""
                                email = ""
                                description = ""
                                addedDate = ""
                            }
                            "id" -> id = parser.nextText()
                            "companyName" -> companyName = parser.nextText()
                            "nationalId" -> nationalId = parser.nextText()
                            "registrationNumber" -> registrationNumber = parser.nextText()
                            "phone" -> phone = parser.nextText()
                            "address" -> address = parser.nextText()
                            "managerName" -> managerName = parser.nextText()
                            "phoneNumber" -> phoneNumber = parser.nextText()
                            "email" -> email = parser.nextText()
                            "description" -> description = parser.nextText()
                            "addedDate" -> addedDate = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "client") {
                            clients.add(LegalClient(
                                id = id,
                                companyName = companyName,
                                nationalId = nationalId,
                                registrationNumber = registrationNumber,
                                phone = phone,
                                address = address,
                                managerName = managerName,
                                phoneNumber = phoneNumber,
                                email = email,
                                description = description,
                                addedDate = addedDate
                            ))
                        }
                    }
                }
                eventType = parser.next()
            }
            clients
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveDailyTasks(context: Context, tasks: List<DailyTask>): Boolean {
        return try {
            val file = File(context.filesDir, "daily_tasks.xml")
            val writer = FileWriter(file)
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            writer.write("<tasks>\n")
            tasks.forEach { task ->
                writer.write("  <task>\n")
                writer.write("    <id>${escapeXml(task.id)}</id>\n")
                writer.write("    <title>${escapeXml(task.title)}</title>\n")
                writer.write("    <description>${escapeXml(task.description)}</description>\n")
                writer.write("    <dueDate>${escapeXml(task.dueDate)}</dueDate>\n")
                writer.write("    <dueTime>${escapeXml(task.dueTime)}</dueTime>\n")
                writer.write("    <priority>${escapeXml(task.priority)}</priority>\n")
                writer.write("    <isCompleted>${task.isCompleted}</isCompleted>\n")
                writer.write("    <addedDate>${escapeXml(task.addedDate)}</addedDate>\n")
                writer.write("  </task>\n")
            }
            writer.write("</tasks>")
            writer.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun loadDailyTasks(context: Context): List<DailyTask> {
        return try {
            val file = File(context.filesDir, "daily_tasks.xml")
            if (!file.exists()) return emptyList()
            
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(FileInputStream(file), "UTF-8")
            
            val tasks = mutableListOf<DailyTask>()
            var eventType = parser.eventType
            
            var id = ""
            var title = ""
            var description = ""
            var dueDate = ""
            var dueTime = ""
            var priority = ""
            var isCompleted = false
            var addedDate = ""
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "task" -> {
                                id = ""
                                title = ""
                                description = ""
                                dueDate = ""
                                dueTime = ""
                                priority = ""
                                isCompleted = false
                                addedDate = ""
                            }
                            "id" -> id = parser.nextText()
                            "title" -> title = parser.nextText()
                            "description" -> description = parser.nextText()
                            "dueDate" -> dueDate = parser.nextText()
                            "dueTime" -> dueTime = parser.nextText()
                            "priority" -> priority = parser.nextText()
                            "isCompleted" -> isCompleted = parser.nextText().toBoolean()
                            "addedDate" -> addedDate = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "task") {
                            tasks.add(DailyTask(
                                id = id,
                                title = title,
                                description = description,
                                dueDate = dueDate,
                                dueTime = dueTime,
                                priority = priority,
                                isCompleted = isCompleted,
                                addedDate = addedDate
                            ))
                        }
                    }
                }
                eventType = parser.next()
            }
            tasks
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveDocuments(context: Context, documents: List<Document>): Boolean {
        return try {
            val file = File(context.filesDir, "documents.xml")
            val writer = FileWriter(file)
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
            writer.write("<documents>\n")
            documents.forEach { document ->
                writer.write("  <document>\n")
                writer.write("    <id>${escapeXml(document.id)}</id>\n")
                writer.write("    <name>${escapeXml(document.name)}</name>\n")
                writer.write("    <filePath>${escapeXml(document.filePath)}</filePath>\n")
                writer.write("    <mimeType>${escapeXml(document.mimeType)}</mimeType>\n")
                writer.write("    <relatedCaseId>${escapeXml(document.relatedCaseId)}</relatedCaseId>\n")
                writer.write("    <fileExtension>${escapeXml(document.fileExtension)}</fileExtension>\n")
                writer.write("    <addedDate>${escapeXml(document.addedDate)}</addedDate>\n")
                writer.write("    <title>${escapeXml(document.title)}</title>\n")
                writer.write("    <caseId>${escapeXml(document.caseId)}</caseId>\n")
                writer.write("  </document>\n")
            }
            writer.write("</documents>")
            writer.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun loadDocuments(context: Context): List<Document> {
        return try {
            val file = File(context.filesDir, "documents.xml")
            if (!file.exists()) return emptyList()
            
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(FileInputStream(file), "UTF-8")
            
            val documents = mutableListOf<Document>()
            var eventType = parser.eventType
            
            var id = ""
            var name = ""
            var filePath = ""
            var mimeType = ""
            var relatedCaseId = ""
            var fileExtension = ""
            var addedDate = ""
            var title = ""
            var caseId = ""
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "document" -> {
                                id = ""
                                name = ""
                                filePath = ""
                                mimeType = ""
                                relatedCaseId = ""
                                fileExtension = ""
                                addedDate = ""
                                title = ""
                                caseId = ""
                            }
                            "id" -> id = parser.nextText()
                            "name" -> name = parser.nextText()
                            "filePath" -> filePath = parser.nextText()
                            "mimeType" -> mimeType = parser.nextText()
                            "relatedCaseId" -> relatedCaseId = parser.nextText()
                            "fileExtension" -> fileExtension = parser.nextText()
                            "addedDate" -> addedDate = parser.nextText()
                            "title" -> title = parser.nextText()
                            "caseId" -> caseId = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "document") {
                            documents.add(Document(
                                id = id,
                                name = name,
                                filePath = filePath,
                                mimeType = mimeType,
                                relatedCaseId = relatedCaseId,
                                fileExtension = fileExtension,
                                addedDate = addedDate,
                                title = title,
                                caseId = caseId
                            ))
                        }
                    }
                }
                eventType = parser.next()
            }
            documents
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addDocument(context: Context, document: Document): Boolean {
        val documents = loadDocuments(context).toMutableList()
        documents.add(document)
        return saveDocuments(context, documents)
    }

    fun deleteDocument(context: Context, documentId: String): Boolean {
        val documents = loadDocuments(context).toMutableList()
        val documentToRemove = documents.find { it.id == documentId }
        documentToRemove?.let {
            val file = File(it.filePath)
            if (file.exists()) {
                file.delete()
            }
            documents.remove(it)
        }
        return saveDocuments(context, documents)
    }

    fun deleteDocumentsForCase(context: Context, caseId: String) {
        val documents = loadDocuments(context).toMutableList()
        val documentsToRemove = documents.filter { it.caseId == caseId }
        documentsToRemove.forEach { document ->
            val file = File(document.filePath)
            if (file.exists()) {
                file.delete()
            }
            documents.remove(document)
        }
        saveDocuments(context, documents)
    }

    private fun escapeXml(text: String): String {
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }
}