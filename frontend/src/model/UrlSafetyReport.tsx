export class UrlSafetyReport {
  status: string;
  safetyParams: SafetyParams;
}

export class SafetyParams {
  siteIsAlive: SafetyStatus;
  syntaxIsCorrect: SafetyStatus;
}

export enum SafetyStatus {
  YES = "YES",
  NO = "NO",
  UNKNOWN = "UNKNOWN",
}
