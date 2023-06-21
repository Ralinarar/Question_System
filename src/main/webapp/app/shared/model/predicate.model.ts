import { IUser } from 'app/shared/model/user.model';
import { ITemplate } from 'app/shared/model/template.model';

export interface IPredicate {
  id?: number;
  rdfValue?: string | null;
  author?: IUser;
  templates?: ITemplate[] | null;
}

export const defaultValue: Readonly<IPredicate> = {};
