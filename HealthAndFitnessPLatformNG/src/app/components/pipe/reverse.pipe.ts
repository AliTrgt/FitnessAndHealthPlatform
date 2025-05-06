import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'reverse',
  standalone: true
})
export class ReversePipe implements PipeTransform {

  transform(value: any[], ...args: unknown[]): any[] {
    if (!Array.isArray(value)) {
      throw new Error('The value provided to reverse pipe must be an array');
    }
    return value.slice().reverse();  // value'yu değiştirmeden ters çevirir
  }

}
