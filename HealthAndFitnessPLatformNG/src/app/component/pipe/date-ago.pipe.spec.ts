import { DateAgoPipe } from '../pipe/date-ago.pipe';

describe('DateAgoPipe', () => {
  it('create an instance', () => {
    const pipe = new DateAgoPipe();
    expect(pipe).toBeTruthy();
  });
});
