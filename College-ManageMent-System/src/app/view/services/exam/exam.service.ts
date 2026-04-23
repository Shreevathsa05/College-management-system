import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ExamReq } from '../../models/request_dto/exam-req';
import { ExamRes } from '../../models/response_dto/exam-res';


@Injectable({
  providedIn: 'root'
})
export class ExamService {

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem("token")}`
    });
  }

  // CREATE
  addExam(data: ExamReq): Observable<any> {
    return this.http.post("/api/exams", data, { headers: this.getHeaders() });
  }

  // READ ALL
  getExams(): Observable<ExamRes[]> {
    return this.http.get<ExamRes[]>("/api/exams", { headers: this.getHeaders() });
  }

  // READ ONE
  getExam(id: number | string): Observable<ExamRes> {
    return this.http.get<ExamRes>(`/api/exams/${id}`, { headers: this.getHeaders() });
  }

  // UPDATE (PUT)
  updateExam(id: number | string, data: ExamReq): Observable<ExamRes> {
    return this.http.put<ExamRes>(`/api/exams/${id}`, data, { headers: this.getHeaders() });
  }

  // PATCH
  patchExam(id: number | string, data: Partial<ExamReq>): Observable<ExamRes> {
    return this.http.patch<ExamRes>(`/api/exams/${id}`, data, { headers: this.getHeaders() });
  }

  // DELETE
  deleteExam(id: number | string): Observable<any> {
    return this.http.delete(`/api/exams/${id}`, { headers: this.getHeaders() });
  }

  // FILTERS
  getUpcoming(): Observable<ExamRes[]> {
    return this.http.get<ExamRes[]>('/api/exams/upcoming', { headers: this.getHeaders() });
  }

  getBySubject(subjectId: number | string): Observable<ExamRes[]> {
    return this.http.get<ExamRes[]>(`/api/exams/subject/${subjectId}`, { headers: this.getHeaders() });
  }

  getByType(examType: string): Observable<ExamRes[]> {
    return this.http.get<ExamRes[]>(`/api/exams/type/${examType}`, { headers: this.getHeaders() });
  }

  getBySession(session: string): Observable<ExamRes[]> {
    return this.http.get<ExamRes[]>(`/api/exams/session/${session}`, { headers: this.getHeaders() });
  }

  getByDateRange(from: string, to: string): Observable<ExamRes[]> {
    return this.http.get<ExamRes[]>(`/api/exams/range?from=${from}&to=${to}`, { headers: this.getHeaders() });
  }

  getByClassroom(classroomId: number | string): Observable<ExamRes[]> {
    return this.http.get<ExamRes[]>(`/api/exams/classroom/${classroomId}`, { headers: this.getHeaders() });
  }
}