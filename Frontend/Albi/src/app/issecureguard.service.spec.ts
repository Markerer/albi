import { TestBed, inject } from '@angular/core/testing';

import { IsSecureGuard } from './issecureguard.service';

describe('IssecureGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [IsSecureGuard]
    });
  });

  it('should be created', inject([IsSecureGuard], (service: IsSecureGuard) => {
    expect(service).toBeTruthy();
  }));
});
