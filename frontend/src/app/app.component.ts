import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MapService } from './services/map.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  @ViewChild('mapContainer', { static: false }) mapElement!: ElementRef;
  
  destination: string = '';
  numDays: number = 3;
  interests: string = '';
  itinerary: any = null;
  loading: boolean = false;
  map!: google.maps.Map;
  markers: google.maps.Marker[] = [];

  constructor(private http: HttpClient, private mapService: MapService) {}

  async ngOnInit() {
    await this.mapService.loadGoogleMaps();
    this.initMap();
  }

  initMap() {
    const defaultLocation = { lat: 20.5937, lng: 78.9629 };
    this.map = new google.maps.Map(this.mapElement.nativeElement, {
      center: defaultLocation,
      zoom: 5,
      styles: [
        {
          "elementType": "geometry",
          "stylers": [{"color": "#212121"}]
        },
        {
          "elementType": "labels.icon",
          "stylers": [{"visibility": "off"}]
        },
        {
          "elementType": "labels.text.fill",
          "stylers": [{"color": "#757575"}]
        },
        {
          "elementType": "labels.text.stroke",
          "stylers": [{"color": "#212121"}]
        },
        {
          "featureType": "administrative",
          "elementType": "geometry",
          "stylers": [{"color": "#757575"}]
        },
        {
          "featureType": "water",
          "elementType": "geometry",
          "stylers": [{"color": "#000000"}]
        },
        {
          "featureType": "water",
          "elementType": "labels.text.fill",
          "stylers": [{"color": "#3d3d3d"}]
        }
      ]
    });
  }

  generatePlan() {
    if (!this.destination) return;
    
    this.loading = true;
    this.itinerary = null;
    this.clearMarkers();

    this.http.get<any>(`/api/plan?destination=${this.destination}&days=${this.numDays}&interests=${this.interests}`).subscribe({
      next: (data) => {
        this.itinerary = data;
        this.loading = false;
        this.updateMap();
      },
      error: (err) => {
        console.error('Error generating plan:', err);
        this.loading = false;
      }
    });
  }

  updateMap() {
    if (!this.itinerary || !this.itinerary.itinerary) return;

    const bounds = new google.maps.LatLngBounds();
    
    this.itinerary.itinerary.forEach((day: any) => {
      day.activities.forEach((activity: any) => {
        const position = { lat: activity.lat, lng: activity.lng };
        const marker = new google.maps.Marker({
          position: position,
          map: this.map,
          title: activity.name,
          animation: google.maps.Animation.DROP,
          label: {
            text: day.day.toString(),
            color: "white",
            fontWeight: "bold"
          }
        });
        
        const infoWindow = new google.maps.InfoWindow({
          content: `
            <div style="color: black; padding: 10px;">
              <h3 style="margin: 0;">${activity.name}</h3>
              <p style="margin: 5px 0 0;">${activity.description}</p>
            </div>
          `
        });

        marker.addListener('click', () => {
          infoWindow.open(this.map, marker);
        });

        this.markers.push(marker);
        bounds.extend(position);
      });
    });

    this.map.fitBounds(bounds);
  }

  clearMarkers() {
    this.markers.forEach(m => m.setMap(null));
    this.markers = [];
  }
}
