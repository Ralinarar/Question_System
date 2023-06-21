import { IUser } from 'app/shared/model/user.model';

export interface IMathtest {
  id?: number;
  amount?: number | null;
  keys?: string | null;
  treshold?: number | null;
  assigneds?: IUser[] | null;
}

export const defaultValue: Readonly<IMathtest> = {};
