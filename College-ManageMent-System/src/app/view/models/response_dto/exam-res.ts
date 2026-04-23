export interface ExamRes {
    id: number;
    examType: string;
    session: string;
    examDate: string; // ISO date format
    startTime?: string;
    endTime?: string;
    maxMarks: number;
    passingMarks?: number;

    classroomId?: number;
    classroomRoomNumber?: string;
    classroomBuilding?: string;

    subjectId: number;
    subjectName: string;
    subjectCode: string;
    semesterNumber?: number;

    courseName?: string;
    departmentName?: string;
}
