export interface ExamReq {
    examType: string;
    session: string;
    examDate: string; // ISO date format YYYY-MM-DD
    startTime?: string; // HH:mm:ss format
    endTime?: string;
    maxMarks: number;
    passingMarks?: number;
    classroomId?: number;
    subjectId: number;
}
