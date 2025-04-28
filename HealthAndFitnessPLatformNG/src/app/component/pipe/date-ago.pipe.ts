import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateAgo',
  standalone: true
})
export class DateAgoPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (!value) return '';

    const seconds = Math.floor((+new Date() - +new Date(value)) / 1000);
    
    if (seconds < 29) // 30 saniyeden az ise "şimdi" döndür
      return 'Şimdi';
    
    const intervals: { [key: string]: number } = {
      'yıl': 31536000,
      'ay': 2592000,
      'hafta': 604800,
      'gün': 86400,
      'saat': 3600,
      'dakika': 60,
      'saniye': 1
    };

    let counter;
    for (const interval in intervals) {
      counter = Math.floor(seconds / intervals[interval]);
      if (counter > 0) {
        if (counter === 1) {
          return counter + ' ' + interval + ' önce'; // Tekil (1 gün)
        } else {
          return counter + ' ' + interval + ' önce'; // Çoğul (2 gün)
        }
      }
    }
    return value;
  }

}
