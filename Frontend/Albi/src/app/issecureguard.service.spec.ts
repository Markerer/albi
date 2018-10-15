import { TestBed, inject } from '@angular/core/testing';

import { IssecureguardService } from './issecureguard.service';

describe('IssecureguardService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [IssecureguardService]
    });
  });

  it('should be created', inject([IssecureguardService], (service: IssecureguardService) => {
    expect(service).toBeTruthy();
  }));
});
