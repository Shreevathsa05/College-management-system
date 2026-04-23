import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { InfrastructureReq } from '../../models/request_dto/infrastructure-req';
import { InfrastructureRes } from '../../models/response_dto/infrastructure-res';

@Injectable({
  providedIn: 'root'
})
export class InfrastructureService {

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      Authorization: `Bearer ${localStorage.getItem("token")}`
    });
  }

  // CREATE
  addInfrastructure(data: InfrastructureReq): Observable<any> {
    return this.http.post("/api/infrastructure", data, { headers: this.getHeaders() });
  }

  // READ ALL
  getAll(): Observable<InfrastructureRes[]> {
    return this.http.get<InfrastructureRes[]>("/api/infrastructure", { headers: this.getHeaders() });
  }

  // READ ONE
  getById(id: number | string): Observable<InfrastructureRes> {
    return this.http.get<InfrastructureRes>(`/api/infrastructure/${id}`, { headers: this.getHeaders() });
  }

  // UPDATE (PUT)
  update(id: number | string, data: InfrastructureReq): Observable<InfrastructureRes> {
    return this.http.put<InfrastructureRes>(`/api/infrastructure/${id}`, data, { headers: this.getHeaders() });
  }

  // PATCH
  patch(id: number | string, data: Partial<InfrastructureReq>): Observable<InfrastructureRes> {
    return this.http.patch<InfrastructureRes>(`/api/infrastructure/${id}`, data, { headers: this.getHeaders() });
  }

  // DELETE
  delete(id: number | string): Observable<any> {
    return this.http.delete(`/api/infrastructure/${id}`, { headers: this.getHeaders() });
  }

  // STATUS PATCH
  updateStatus(id: number | string, status: string): Observable<InfrastructureRes> {
    return this.http.patch<InfrastructureRes>(`/api/infrastructure/${id}/status?value=${status}`, {}, { headers: this.getHeaders() });
  }

  // FILTER METHODS
  getByDepartment(deptId: number | string): Observable<InfrastructureRes[]> {
    return this.http.get<InfrastructureRes[]>(`/api/infrastructure/department/${deptId}`, { headers: this.getHeaders() });
  }

  getByStatus(status: string): Observable<InfrastructureRes[]> {
    return this.http.get<InfrastructureRes[]>(`/api/infrastructure/status/${status}`, { headers: this.getHeaders() });
  }

  getByType(type: string): Observable<InfrastructureRes[]> {
    return this.http.get<InfrastructureRes[]>(`/api/infrastructure/type/${type}`, { headers: this.getHeaders() });
  }

  getByBlock(block: string): Observable<InfrastructureRes[]> {
    return this.http.get<InfrastructureRes[]>(`/api/infrastructure/block/${block}`, { headers: this.getHeaders() });
  }
}