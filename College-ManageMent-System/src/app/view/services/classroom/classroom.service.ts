import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClassroomReq } from '../../models/request_dto/classroom-req';
import { ClassroomRes } from '../../models/response_dto/classroom-res';

@Injectable({
  providedIn: 'root'
})
export class ClassroomService {

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem("token")}`
    });
  }

  // CREATE
  addClassroom(data: ClassroomReq): Observable<any> {
    return this.http.post("/api/classrooms", data, { headers: this.getHeaders() });
  }

  // READ ALL
  getClassrooms(): Observable<ClassroomRes[]> {
    return this.http.get<ClassroomRes[]>("/api/classrooms", { headers: this.getHeaders() });
  }

  // READ ONE
  getClassroom(id: number | string): Observable<ClassroomRes> {
    return this.http.get<ClassroomRes>(`/api/classrooms/${id}`, { headers: this.getHeaders() });
  }

  // UPDATE (PUT)
  updateClassroom(id: number | string, data: ClassroomReq): Observable<ClassroomRes> {
    return this.http.put<ClassroomRes>(`/api/classrooms/${id}`, data, { headers: this.getHeaders() });
  }

  // PATCH
  patchClassroom(id: number | string, data: Partial<ClassroomReq>): Observable<ClassroomRes> {
    return this.http.patch<ClassroomRes>(`/api/classrooms/${id}`, data, { headers: this.getHeaders() });
  }

  // DELETE
  deleteClassroom(id: number | string): Observable<any> {
    return this.http.delete(`/api/classrooms/${id}`, { headers: this.getHeaders() });
  }

  // AVAILABILITY PATCH
  toggleAvailability(id: number | string, isAvailable: boolean): Observable<ClassroomRes> {
    return this.http.patch<ClassroomRes>(`/api/classrooms/${id}/availability?value=${isAvailable}`, {}, { headers: this.getHeaders() });
  }

  // FILTERS
  getAvailable(): Observable<ClassroomRes[]> {
    return this.http.get<ClassroomRes[]>('/api/classrooms/available', { headers: this.getHeaders() });
  }

  getByType(type: string): Observable<ClassroomRes[]> {
    return this.http.get<ClassroomRes[]>(`/api/classrooms/type/${type}`, { headers: this.getHeaders() });
  }

  getByDepartment(deptId: number | string): Observable<ClassroomRes[]> {
    return this.http.get<ClassroomRes[]>(`/api/classrooms/department/${deptId}`, { headers: this.getHeaders() });
  }
}