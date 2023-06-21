import { IUser } from 'app/shared/model/user.model';

export interface ITemplate {
  id?: number;
  mock?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ITemplate> = {};
