import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { FacultyReq } from '../../models/request_dto/faculty-req';
import { FacultyRes } from '../../models/response_dto/faculty-res';

@Injectable({
  providedIn: 'root'
})
export class FacultyService {

  private baseUrl = 'http://localhost:8080/api/faculty'; // ✅ FIXED

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem("token") || ''}`
    });
  }

  // ================= CREATE =================
  addFaculty(data: FacultyReq): Observable<any> {
    return this.http.post(this.baseUrl, data, { headers: this.getHeaders() });
  }

  // ================= READ ALL =================
  getFacultyList(): Observable<FacultyRes[]> {
    return this.http.get<FacultyRes[]>(this.baseUrl, { headers: this.getHeaders() });
  }

  // ================= READ ONE =================
  getFaculty(id: number | string): Observable<FacultyRes> {
    return this.http.get<FacultyRes>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }

  // ================= UPDATE =================
  updateFaculty(id: number | string, data: FacultyReq): Observable<FacultyRes> {
    return this.http.put<FacultyRes>(`${this.baseUrl}/${id}`, data, { headers: this.getHeaders() });
  }

  // ================= DELETE =================
  deleteFaculty(id: number | string): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }
}
