package com.lawyer_archives.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.lawyer_archives.models.Case
import com.lawyer_archives.models.Client
import com.lawyer_archives.models.CourtSession
import com.lawyer_archives.models.DailyTask
import com.lawyer_archives.models.Meeting

/**
 * Helper class for managing the SQLite database.
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        /** Name of the database file. */
        private const val DATABASE_NAME = "lawyer_archives.db"

        /** Database version for upgrade handling. */
        private const val DATABASE_VERSION = 2

        /** Name of the daily tasks table. */
        public const val TABLE_DAILY_TASKS = "daily_tasks"

        /** Name of the clients table. */
        public const val TABLE_CLIENTS = "clients"

        /** Name of the cases table. */
        public const val TABLE_CASES = "cases"

        /** Name of the meetings table. */
        public const val TABLE_MEETINGS = "meetings"

        /** Name of the court sessions table. */
        public const val TABLE_COURT_SESSIONS = "court_sessions"

        /** Common column for unique identifier. */
        public const val COLUMN_ID = "id"

        /** Common column for user identifier. */
        public const val COLUMN_USER_ID = "user_id"

        /** Common column for added date. */
        public const val COLUMN_ADDED_DATE = "added_date"

        /** Daily task column for title. */
        public const val COLUMN_TITLE = "title"

        /** Daily task column for description. */
        public const val COLUMN_DESCRIPTION = "description"

        /** Daily task column for due date. */
        public const val COLUMN_DUE_DATE = "due_date"

        /** Daily task column for due time. */
        public const val COLUMN_DUE_TIME = "due_time"

        /** Daily task column for priority. */
        public const val COLUMN_PRIORITY = "priority"

        /** Daily task column for completion status. */
        public const val COLUMN_IS_COMPLETED = "is_completed"

        /** Daily task column for related client or case. */
        public const val COLUMN_RELATED_CLIENT_OR_CASE = "related_client_or_case"

        /** Client column for name. */
        public const val COLUMN_NAME = "name"

        /** Client column for phone. */
        public const val COLUMN_PHONE = "phone"

        /** Client column for email. */
        public const val COLUMN_EMAIL = "email"

        /** Client column for type. */
        public const val COLUMN_TYPE = "type"

        /** Client column for national code. */
        public const val COLUMN_NATIONAL_CODE = "national_code"

        /** Client column for job. */
        public const val COLUMN_JOB = "job"

        /** Client column for mobile phone. */
        public const val COLUMN_MOBILE_PHONE = "mobile_phone"

        /** Client column for landline. */
        public const val COLUMN_LANDLINE = "landline"

        /** Client column for home address. */
        public const val COLUMN_HOME_ADDRESS = "home_address"

        /** Client column for work address. */
        public const val COLUMN_WORK_ADDRESS = "work_address"

        /** Case column for case title. */
        public const val COLUMN_CASE_TITLE = "case_title"

        /** Case column for formation date. */
        public const val COLUMN_FORMATION_DATE = "formation_date"

        /** Case column for client name. */
        public const val COLUMN_CLIENT_NAME = "client_name"

        /** Case column for client role. */
        public const val COLUMN_CLIENT_ROLE = "client_role"

        /** Case column for case subject. */
        public const val COLUMN_CASE_SUBJECT = "case_subject"

        /** Case column for status. */
        public const val COLUMN_STATUS = "status"

        /** Case column for process. */
        public const val COLUMN_PROCESS = "process"

        /** Case column for case number. */
        public const val COLUMN_CASE_NUMBER = "case_number"

        /** Case column for archive number. */
        public const val COLUMN_ARCHIVE_NUMBER = "archive_number"

        /** Case column for city judiciary. */
        public const val COLUMN_CITY_JUDICIARY = "city_judiciary"

        /** Case column for court level and type. */
        public const val COLUMN_COURT_LEVEL_AND_TYPE = "court_level_and_type"

        /** Case column for opponent info. */
        public const val COLUMN_OPPONENT_INFO = "opponent_info"

        /** Case column for power of attorney number. */
        public const val COLUMN_POWER_OF_ATTORNEY_NUMBER = "power_of_attorney_number"

        /** Case column for court date. */
        public const val COLUMN_COURT_DATE = "court_date"

        /** Meeting column for meeting title. */
        public const val COLUMN_MEETING_TITLE = "meeting_title"

        /** Meeting column for meeting date. */
        public const val COLUMN_MEETING_DATE = "meeting_date"

        /** Meeting column for meeting time. */
        public const val COLUMN_MEETING_TIME = "meeting_time"

        /** Meeting column for location. */
        public const val COLUMN_LOCATION = "location"

        /** Meeting column for meeting description. */
        public const val COLUMN_MEETING_DESCRIPTION = "meeting_description"

        /** Meeting column for reminder option. */
        public const val COLUMN_REMINDER_OPTION = "reminder_option"

        /** Court session column for session title. */
        public const val COLUMN_SESSION_TITLE = "session_title"

        /** Court session column for session description. */
        public const val COLUMN_SESSION_DESCRIPTION = "session_description"

        /** Court session column for session date. */
        public const val COLUMN_SESSION_DATE = "session_date"

        /** Court session column for session time. */
        public const val COLUMN_SESSION_TIME = "session_time"

        /** Court session column for court branch. */
        public const val COLUMN_COURT_BRANCH = "court_branch"

        /** Court session column for session status. */
        public const val COLUMN_SESSION_STATUS = "session_status"

        /** Court session column for case title reference. */
        public const val COLUMN_CASE_TITLE_REF = "case_title_ref"

        /** Court session column for session location. */
        public const val COLUMN_SESSION_LOCATION = "session_location"

        /** Court session column for case ID. */
        public const val COLUMN_CASE_ID = "case_id"

        /** Court session column for completion status. */
        public const val COLUMN_IS_COMPLETED_SESSION = "is_completed"
    }

    override fun onCreate(db: SQLiteDatabase) {
        createDailyTasksTable(db)
        createClientsTable(db)
        createCasesTable(db)
        createMeetingsTable(db)
        createCourtSessionsTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_COURT_SESSIONS ADD COLUMN $COLUMN_SESSION_DESCRIPTION TEXT")
        }
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DAILY_TASKS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CASES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MEETINGS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COURT_SESSIONS")
        onCreate(db)
    }

    private fun createDailyTasksTable(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE IF NOT EXISTS $TABLE_DAILY_TASKS (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_USER_ID TEXT NOT NULL,
                $COLUMN_TITLE TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_DUE_DATE TEXT,
                $COLUMN_DUE_TIME TEXT,
                $COLUMN_PRIORITY TEXT,
                $COLUMN_IS_COMPLETED INTEGER DEFAULT 0,
                $COLUMN_ADDED_DATE TEXT,
                $COLUMN_RELATED_CLIENT_OR_CASE TEXT
            )
        """.trimIndent()
        db.execSQL(sql)
    }

    private fun createClientsTable(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE IF NOT EXISTS $TABLE_CLIENTS (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_USER_ID TEXT NOT NULL,
                $COLUMN_NAME TEXT,
                $COLUMN_PHONE TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_TYPE TEXT,
                $COLUMN_NATIONAL_CODE TEXT,
                $COLUMN_JOB TEXT,
                $COLUMN_MOBILE_PHONE TEXT,
                $COLUMN_LANDLINE TEXT,
                $COLUMN_HOME_ADDRESS TEXT,
                $COLUMN_WORK_ADDRESS TEXT,
                $COLUMN_ADDED_DATE TEXT
            )
        """.trimIndent()
        db.execSQL(sql)
    }

    private fun createCasesTable(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE IF NOT EXISTS $TABLE_CASES (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_USER_ID TEXT NOT NULL,
                $COLUMN_CASE_TITLE TEXT,
                $COLUMN_FORMATION_DATE TEXT,
                $COLUMN_CLIENT_NAME TEXT,
                $COLUMN_CLIENT_ROLE TEXT,
                $COLUMN_CASE_SUBJECT TEXT,
                $COLUMN_STATUS TEXT,
                $COLUMN_PROCESS TEXT,
                $COLUMN_CASE_NUMBER TEXT,
                $COLUMN_ARCHIVE_NUMBER TEXT,
                $COLUMN_CITY_JUDICIARY TEXT,
                $COLUMN_COURT_LEVEL_AND_TYPE TEXT,
                $COLUMN_OPPONENT_INFO TEXT,
                $COLUMN_POWER_OF_ATTORNEY_NUMBER TEXT,
                $COLUMN_COURT_DATE TEXT,
                $COLUMN_ADDED_DATE TEXT
            )
        """.trimIndent()
        db.execSQL(sql)
    }

    private fun createMeetingsTable(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE IF NOT EXISTS $TABLE_MEETINGS (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_USER_ID TEXT NOT NULL,
                $COLUMN_MEETING_TITLE TEXT,
                $COLUMN_MEETING_DATE TEXT,
                $COLUMN_MEETING_TIME TEXT,
                $COLUMN_LOCATION TEXT,
                $COLUMN_MEETING_DESCRIPTION TEXT,
                $COLUMN_REMINDER_OPTION TEXT,
                $COLUMN_ADDED_DATE TEXT
            )
        """.trimIndent()
        db.execSQL(sql)
    }

    private fun createCourtSessionsTable(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE IF NOT EXISTS $TABLE_COURT_SESSIONS (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_USER_ID TEXT NOT NULL,
                $COLUMN_SESSION_TITLE TEXT,
                $COLUMN_SESSION_DESCRIPTION TEXT,
                $COLUMN_SESSION_DATE TEXT,
                $COLUMN_SESSION_TIME TEXT,
                $COLUMN_COURT_BRANCH TEXT,
                $COLUMN_SESSION_STATUS TEXT,
                $COLUMN_CASE_TITLE_REF TEXT,
                $COLUMN_SESSION_LOCATION TEXT,
                $COLUMN_CASE_ID TEXT,
                $COLUMN_IS_COMPLETED_SESSION INTEGER DEFAULT 0,
                $COLUMN_ADDED_DATE TEXT
            )
        """.trimIndent()
        db.execSQL(sql)
    }

    // ========== Daily Tasks Methods ==========
    fun insertDailyTask(task: DailyTask): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, task.id)
            put(COLUMN_USER_ID, task.userId)
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_DUE_DATE, task.dueDate)
            put(COLUMN_DUE_TIME, task.dueTime)
            put(COLUMN_PRIORITY, task.priority)
            put(COLUMN_IS_COMPLETED, if (task.isCompleted) 1 else 0)
            put(COLUMN_ADDED_DATE, task.addedDate)
            put(COLUMN_RELATED_CLIENT_OR_CASE, task.relatedClientOrCase)
        }
        return db.insert(TABLE_DAILY_TASKS, null, values)
    }

    fun getDailyTasksByUserId(userId: String): List<DailyTask> {
        val tasks = mutableListOf<DailyTask>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_DAILY_TASKS,
            null,
            "$COLUMN_USER_ID = ?",
            arrayOf(userId),
            null, null, "$COLUMN_ADDED_DATE DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                tasks.add(DailyTask(
                    id = it.getString(it.getColumnIndexOrThrow(COLUMN_ID)),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE)),
                    description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    dueDate = it.getString(it.getColumnIndexOrThrow(COLUMN_DUE_DATE)),
                    dueTime = it.getString(it.getColumnIndexOrThrow(COLUMN_DUE_TIME)),
                    priority = it.getString(it.getColumnIndexOrThrow(COLUMN_PRIORITY)),
                    isCompleted = it.getInt(it.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1,
                    addedDate = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDED_DATE)),
                    relatedClientOrCase = it.getString(it.getColumnIndexOrThrow(COLUMN_RELATED_CLIENT_OR_CASE))
                ))
            }
        }
        return tasks
    }

    fun updateDailyTask(task: DailyTask): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_DUE_DATE, task.dueDate)
            put(COLUMN_DUE_TIME, task.dueTime)
            put(COLUMN_PRIORITY, task.priority)
            put(COLUMN_IS_COMPLETED, if (task.isCompleted) 1 else 0)
            put(COLUMN_RELATED_CLIENT_OR_CASE, task.relatedClientOrCase)
        }
        return db.update(TABLE_DAILY_TASKS, values, "$COLUMN_ID = ?", arrayOf(task.id))
    }

    fun deleteDailyTask(taskId: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_DAILY_TASKS, "$COLUMN_ID = ?", arrayOf(taskId))
    }

    fun getDailyTaskById(taskId: String): DailyTask? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_DAILY_TASKS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(taskId),
            null, null, null
        )

        return cursor.use {
            if (it.moveToFirst()) {
                DailyTask(
                    id = it.getString(it.getColumnIndexOrThrow(COLUMN_ID)),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE)),
                    description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    dueDate = it.getString(it.getColumnIndexOrThrow(COLUMN_DUE_DATE)),
                    dueTime = it.getString(it.getColumnIndexOrThrow(COLUMN_DUE_TIME)),
                    priority = it.getString(it.getColumnIndexOrThrow(COLUMN_PRIORITY)),
                    isCompleted = it.getInt(it.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1,
                    addedDate = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDED_DATE)),
                    relatedClientOrCase = it.getString(it.getColumnIndexOrThrow(COLUMN_RELATED_CLIENT_OR_CASE))
                )
            } else {
                null
            }
        }
    }

    // ========== Clients Methods ==========
    fun insertClient(client: Client, userId: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, client.id)
            put(COLUMN_USER_ID, userId)
            put(COLUMN_NAME, client.name)
            put(COLUMN_PHONE, client.phoneNumber) // استفاده از phoneNumber
            put(COLUMN_EMAIL, client.email)
            put(COLUMN_TYPE, client.type)
            put(COLUMN_NATIONAL_CODE, client.nationalCode)
            put(COLUMN_JOB, client.job)
            put(COLUMN_MOBILE_PHONE, client.mobilePhone)
            put(COLUMN_LANDLINE, client.landline)
            put(COLUMN_HOME_ADDRESS, client.homeAddress)
            put(COLUMN_WORK_ADDRESS, client.workAddress)
            put(COLUMN_ADDED_DATE, client.addedDate)
        }
        return db.insert(TABLE_CLIENTS, null, values)
    }

    fun getClientsByUserId(userId: String): List<Client> {
        val clients = mutableListOf<Client>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CLIENTS,
            null,
            "$COLUMN_USER_ID = ?",
            arrayOf(userId),
            null, null, "$COLUMN_ADDED_DATE DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                clients.add(Client(
                    id = it.getString(it.getColumnIndexOrThrow(COLUMN_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                    phoneNumber = it.getString(it.getColumnIndexOrThrow(COLUMN_PHONE)),
                    addedDate = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDED_DATE)),
                    email = it.getString(it.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    type = it.getString(it.getColumnIndexOrThrow(COLUMN_TYPE)),
                    nationalCode = it.getString(it.getColumnIndexOrThrow(COLUMN_NATIONAL_CODE)),
                    job = it.getString(it.getColumnIndexOrThrow(COLUMN_JOB)),
                    mobilePhone = it.getString(it.getColumnIndexOrThrow(COLUMN_MOBILE_PHONE)),
                    landline = it.getString(it.getColumnIndexOrThrow(COLUMN_LANDLINE)),
                    homeAddress = it.getString(it.getColumnIndexOrThrow(COLUMN_HOME_ADDRESS)),
                    workAddress = it.getString(it.getColumnIndexOrThrow(COLUMN_WORK_ADDRESS))
                ))
            }
        }
        return clients
    }

    fun deleteClient(clientId: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_CLIENTS, "$COLUMN_ID = ?", arrayOf(clientId))
    }

    // ========== Cases Methods ==========
    fun insertCase(case: Case, userId: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, case.id)
            put(COLUMN_USER_ID, userId)
            put(COLUMN_CASE_TITLE, case.title) // استفاده از title به جای caseTitle
            put(COLUMN_FORMATION_DATE, case.formationDate)
            put(COLUMN_CLIENT_NAME, case.clientName)
            put(COLUMN_CLIENT_ROLE, case.clientRole)
            put(COLUMN_CASE_SUBJECT, case.caseSubject)
            put(COLUMN_STATUS, case.status)
            put(COLUMN_PROCESS, case.process)
            put(COLUMN_CASE_NUMBER, case.caseNumber)
            put(COLUMN_ARCHIVE_NUMBER, case.archiveNumber)
            put(COLUMN_CITY_JUDICIARY, case.cityJudiciary)
            put(COLUMN_COURT_LEVEL_AND_TYPE, case.courtLevelAndType)
            put(COLUMN_OPPONENT_INFO, case.opponentInfo)
            put(COLUMN_POWER_OF_ATTORNEY_NUMBER, case.powerOfAttorneyNumber)
            put(COLUMN_COURT_DATE, case.courtDate)
            put(COLUMN_ADDED_DATE, case.addedDate)
        }
        return db.insert(TABLE_CASES, null, values)
    }

    fun getCasesByUserId(userId: String): List<Case> {
        val cases = mutableListOf<Case>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CASES,
            null,
            "$COLUMN_USER_ID = ?",
            arrayOf(userId),
            null, null, "$COLUMN_ADDED_DATE DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                cases.add(Case(
                    id = it.getString(it.getColumnIndexOrThrow(COLUMN_ID)),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_CASE_TITLE)),
                    formationDate = it.getString(it.getColumnIndexOrThrow(COLUMN_FORMATION_DATE)),
                    clientName = it.getString(it.getColumnIndexOrThrow(COLUMN_CLIENT_NAME)),
                    clientRole = it.getString(it.getColumnIndexOrThrow(COLUMN_CLIENT_ROLE)),
                    caseSubject = it.getString(it.getColumnIndexOrThrow(COLUMN_CASE_SUBJECT)),
                    status = it.getString(it.getColumnIndexOrThrow(COLUMN_STATUS)),
                    process = it.getString(it.getColumnIndexOrThrow(COLUMN_PROCESS)),
                    caseNumber = it.getString(it.getColumnIndexOrThrow(COLUMN_CASE_NUMBER)),
                    archiveNumber = it.getString(it.getColumnIndexOrThrow(COLUMN_ARCHIVE_NUMBER)),
                    cityJudiciary = it.getString(it.getColumnIndexOrThrow(COLUMN_CITY_JUDICIARY)),
                    courtLevelAndType = it.getString(it.getColumnIndexOrThrow(COLUMN_COURT_LEVEL_AND_TYPE)),
                    opponentInfo = it.getString(it.getColumnIndexOrThrow(COLUMN_OPPONENT_INFO)),
                    powerOfAttorneyNumber = it.getString(it.getColumnIndexOrThrow(COLUMN_POWER_OF_ATTORNEY_NUMBER)),
                    courtDate = it.getString(it.getColumnIndexOrThrow(COLUMN_COURT_DATE)),
                    addedDate = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDED_DATE))
                ))
            }
        }
        return cases
    }

    fun deleteCase(caseId: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_CASES, "$COLUMN_ID = ?", arrayOf(caseId))
    }

    // ========== Meetings Methods ==========
    fun insertMeeting(meeting: Meeting, userId: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, meeting.id)
            put(COLUMN_USER_ID, userId)
            put(COLUMN_MEETING_TITLE, meeting.title)
            put(COLUMN_MEETING_DATE, meeting.date)
            put(COLUMN_MEETING_TIME, meeting.time)
            // ستون location در جدول وجود دارد اما در مدل Meeting وجود ندارد
            // ستون client_name در جدول وجود ندارد، پس نمی‌توانیم ذخیره کنیم
            put(COLUMN_MEETING_DESCRIPTION, meeting.description)
            put(COLUMN_REMINDER_OPTION, meeting.reminderOption)
            put(COLUMN_ADDED_DATE, meeting.addedDate)
        }
        return db.insert(TABLE_MEETINGS, null, values)
    }

    fun getMeetingsByUserId(userId: String): List<Meeting> {
        val meetings = mutableListOf<Meeting>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_MEETINGS,
            null,
            "$COLUMN_USER_ID = ?",
            arrayOf(userId),
            null, null, "$COLUMN_ADDED_DATE DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                // ساخت Meeting بدون clientName چون در جدول ذخیره نمی‌شود
                meetings.add(Meeting(
                    id = it.getString(it.getColumnIndexOrThrow(COLUMN_ID)),
                    clientName = "", // نمی‌توانیم از دیتابیس بخوانیم چون ذخیره نشده
                    date = it.getString(it.getColumnIndexOrThrow(COLUMN_MEETING_DATE)),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_MEETING_TITLE)),
                    time = it.getString(it.getColumnIndexOrThrow(COLUMN_MEETING_TIME)),
                    description = it.getString(it.getColumnIndexOrThrow(COLUMN_MEETING_DESCRIPTION)),
                    reminderOption = it.getString(it.getColumnIndexOrThrow(COLUMN_REMINDER_OPTION)),
                    addedDate = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDED_DATE))
                ))
            }
        }
        return meetings
    }

    fun deleteMeeting(meetingId: String): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_MEETINGS, "$COLUMN_ID = ?", arrayOf(meetingId))
        return result > 0
    }

    // ========== Court Sessions Methods ==========
    fun insertCourtSession(session: CourtSession, userId: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, session.id)
            put(COLUMN_USER_ID, userId)
            put(COLUMN_SESSION_TITLE, session.title)
            put(COLUMN_SESSION_DESCRIPTION, session.description)
            // استفاده از sessionDate برای ذخیره courtDate
            put(COLUMN_SESSION_DATE, session.courtDate)
            // استفاده از sessionTime برای ذخیره courtTime
            put(COLUMN_SESSION_TIME, session.courtTime)
            put(COLUMN_COURT_BRANCH, session.courtBranch)
            put(COLUMN_SESSION_STATUS, session.status)
            // استفاده از case_title_ref برای ذخیره caseTitle
            put(COLUMN_CASE_TITLE_REF, session.caseTitle)
            put(COLUMN_SESSION_LOCATION, session.location)
            put(COLUMN_CASE_ID, session.caseId)
            put(COLUMN_IS_COMPLETED_SESSION, if (session.isCompleted) 1 else 0)
            put(COLUMN_ADDED_DATE, session.addedDate)
        }
        return db.insert(TABLE_COURT_SESSIONS, null, values)
    }

    fun getCourtSessionsByUserId(userId: String): List<CourtSession> {
        val sessions = mutableListOf<CourtSession>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_COURT_SESSIONS,
            null,
            "$COLUMN_USER_ID = ?",
            arrayOf(userId),
            null, null, "$COLUMN_ADDED_DATE DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                sessions.add(CourtSession(
                    id = it.getString(it.getColumnIndexOrThrow(COLUMN_ID)),
                    title = it.getString(it.getColumnIndexOrThrow(COLUMN_SESSION_TITLE)),
                    description = it.getString(it.getColumnIndexOrThrow(COLUMN_SESSION_DESCRIPTION)),
                    clientName = "", // در جدول ذخیره نمی‌شود
                    courtDate = it.getString(it.getColumnIndexOrThrow(COLUMN_SESSION_DATE)),
                    courtTime = it.getString(it.getColumnIndexOrThrow(COLUMN_SESSION_TIME)),
                    courtBranch = it.getString(it.getColumnIndexOrThrow(COLUMN_COURT_BRANCH)),
                    status = it.getString(it.getColumnIndexOrThrow(COLUMN_SESSION_STATUS)),
                    addedDate = it.getString(it.getColumnIndexOrThrow(COLUMN_ADDED_DATE)),
                    caseTitle = it.getString(it.getColumnIndexOrThrow(COLUMN_CASE_TITLE_REF)),
                    sessionDate = it.getString(it.getColumnIndexOrThrow(COLUMN_SESSION_DATE)),
                    location = it.getString(it.getColumnIndexOrThrow(COLUMN_SESSION_LOCATION)),
                    caseId = it.getString(it.getColumnIndexOrThrow(COLUMN_CASE_ID)),
                    isCompleted = it.getInt(it.getColumnIndexOrThrow(COLUMN_IS_COMPLETED_SESSION)) == 1
                ))
            }
        }
        return sessions
    }

    fun deleteCourtSession(sessionId: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_COURT_SESSIONS, "$COLUMN_ID = ?", arrayOf(sessionId))
    }
}