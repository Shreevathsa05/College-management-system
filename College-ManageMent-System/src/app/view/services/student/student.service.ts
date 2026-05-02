import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StudentReq } from '../../models/request_dto/student-req';
import { StudentRes } from '../../models/response_dto/student-res';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private baseUrl = 'http://localhost:8080/api/students';

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem("token")}`
    });
  }

  // CREATE
  addStudent(data: StudentReq): Observable<any> {
    return this.http.post(this.baseUrl, data, { headers: this.getHeaders() });
  }

  // READ ALL
  getStudents(): Observable<StudentRes[]> {
    return this.http.get<StudentRes[]>(this.baseUrl, { headers: this.getHeaders() });
  }

  // READ ONE
  getStudent(id: number | string): Observable<StudentRes> {
    return this.http.get<StudentRes>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }

  // UPDATE (PUT)
  updateStudent(id: number | string, data: StudentReq): Observable<StudentRes> {
    return this.http.put<StudentRes>(`${this.baseUrl}/${id}`, data, { headers: this.getHeaders() });
  }

  // PATCH
  patchStudent(id: number | string, data: Partial<StudentReq>): Observable<StudentRes> {
    return this.http.patch<StudentRes>(`${this.baseUrl}/${id}`, data, { headers: this.getHeaders() });
  }

  // DELETE
  deleteStudent(id: number | string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }

  // SEARCH
  searchStudents(keyword: string): Observable<StudentRes[]> {
    return this.http.get<StudentRes[]>(`${this.baseUrl}/search?keyword=${encodeURIComponent(keyword)}`, { headers: this.getHeaders() });
  }

  // GET DEPARTMENTS
getDepartments(): Observable<any[]> {
  return this.http.get<any[]>('http://localhost:8080/api/departments', {
    headers: this.getHeaders()
  });
}

// GET COURSES
getCourses(): Observable<any[]> {
  return this.http.get<any[]>('http://localhost:8080/api/courses', {
    headers: this.getHeaders()
  });
}
}