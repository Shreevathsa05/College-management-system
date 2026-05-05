import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { StudentReq } from '../../models/request_dto/student-req';
import { StudentRes } from '../../models/response_dto/student-res';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private baseUrl = 'http://localhost:8080/api/students';

  // 🔥 CACHE (LIST)
  private studentsCache: StudentRes[] | null = null;

  // 🔥 CACHE (DETAILS BY ID)
  private studentDetailsCache: { [key: string]: StudentRes } = {};

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem("token")}`
    });
  }

  // ============================
  // ✅ GET ALL STUDENTS (CACHED)
  // ============================
  getStudents(): Observable<StudentRes[]> {
    if (this.studentsCache) {
      return of(this.studentsCache);
    }

    return this.http.get<StudentRes[]>(this.baseUrl, { headers: this.getHeaders() }).pipe(
      tap(data => {
        this.studentsCache = data;

        // 🔥 also populate details cache
        data.forEach(student => {
          this.studentDetailsCache[student.id] = student;
        });
      })
    );
  }

  // ============================
  // ✅ GET SINGLE STUDENT (CACHED)
  // ============================
  getStudent(id: number | string): Observable<StudentRes> {
    const key = id.toString();

    if (this.studentDetailsCache[key]) {
      return of(this.studentDetailsCache[key]);
    }

    return this.http.get<StudentRes>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() }).pipe(
      tap(data => {
        this.studentDetailsCache[key] = data;
      })
    );
  }

  // ============================
  // ✅ CREATE
  // ============================
  addStudent(data: StudentReq): Observable<any> {
    return this.http.post(this.baseUrl, data, { headers: this.getHeaders() }).pipe(
      tap(() => this.clearCache())
    );
  }

  // ============================
  // ✅ UPDATE
  // ============================
  updateStudent(id: number | string, data: StudentReq): Observable<StudentRes> {
    return this.http.put<StudentRes>(`${this.baseUrl}/${id}`, data, { headers: this.getHeaders() }).pipe(
      tap(() => this.clearCache())
    );
  }

  // ============================
  // ✅ PATCH
  // ============================
  patchStudent(id: number | string, data: Partial<StudentReq>): Observable<StudentRes> {
    return this.http.patch<StudentRes>(`${this.baseUrl}/${id}`, data, { headers: this.getHeaders() }).pipe(
      tap(() => this.clearCache())
    );
  }

  // ============================
  // ✅ DELETE
  // ============================
  deleteStudent(id: number | string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { headers: this.getHeaders() }).pipe(
      tap(() => this.clearCache())
    );
  }

  // ============================
  // ❌ SEARCH (NO CACHE)
  // ============================
  searchStudents(keyword: string): Observable<StudentRes[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<StudentRes[]>(`${this.baseUrl}/search`, {
      headers: this.getHeaders(),
      params
    });
  }

  // ============================
  // ❌ FILTER (NO CACHE)
  // ============================
  filterStudents(
    departmentId?: number | null,
    courseId?: number | null,
    status?: string | null,
    semester?: number | null,
    passoutYear?: number | null
  ): Observable<StudentRes[]> {

    let params = new HttpParams();

    if (departmentId != null) {
      params = params.set('departmentId', departmentId.toString());
    }
    if (courseId != null) {
      params = params.set('courseId', courseId.toString());
    }
    if (status) {
      params = params.set('status', status);
    }
    if (semester != null) {
      params = params.set('semester', semester.toString());
    }
    if (passoutYear != null) {
      params = params.set('passoutYear', passoutYear.toString());
    }

    return this.http.get<StudentRes[]>(`${this.baseUrl}/filter`, {
      headers: this.getHeaders(),
      params
    });
  }

  // ============================
  // EXTRA APIs
  // ============================
  getDepartments(): Observable<any[]> {
    return this.http.get<any[]>('/api/departments', {
      headers: this.getHeaders()
    });
  }

  getCourses(): Observable<any[]> {
    return this.http.get<any[]>('/api/courses', {
      headers: this.getHeaders()
    });
  }

  // ============================
  // 🔥 CLEAR CACHE
  // ============================
  private clearCache() {
    this.studentsCache = null;
    this.studentDetailsCache = {};
  }
}