import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MapService {
  private scriptLoaded = false;

  constructor(private http: HttpClient) {}

  async loadGoogleMaps(): Promise<void> {
    if (this.scriptLoaded) return;

    try {
      const config = await firstValueFrom(this.http.get<{key: string}>('/api/config/maps-key'));
      const apiKey = config.key;

      if (!apiKey) {
        console.error('Google Maps API key not found');
        return;
      }

      return new Promise((resolve, reject) => {
        const script = document.createElement('script');
        script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&callback=initMap`;
        script.async = true;
        script.defer = true;
        script.onload = () => {
          this.scriptLoaded = true;
          resolve();
        };
        script.onerror = (err) => reject(err);
        document.head.appendChild(script);
        (window as any).initMap = () => {}; // Prevent callback error
      });
    } catch (error) {
      console.error('Error loading Google Maps API key:', error);
    }
  }
}
