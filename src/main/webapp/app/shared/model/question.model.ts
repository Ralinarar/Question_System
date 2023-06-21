import { IMathtest } from 'app/shared/model/mathtest.model';

export interface IQuestion {
  id?: number;
  plain?: string | null;
  answer?: string | null;
  test?: IMathtest | null;
}

export const defaultValue: Readonly<IQuestion> = {};
