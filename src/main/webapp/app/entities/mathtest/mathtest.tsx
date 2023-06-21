import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';


import {APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES} from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMathtest } from 'app/shared/model/mathtest.model';
import { getEntities } from './mathtest.reducer';
import {hasAnyAuthority} from "app/shared/auth/private-route";




export const Mathtest = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const mathtestList = useAppSelector(state => state.mathtest.entities);
  const loading = useAppSelector(state => state.mathtest.loading);
  const totalItems = useAppSelector(state => state.mathtest.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="mathtest-heading" data-cy="MathtestHeading">
        <Translate contentKey="qsApp.mathtest.home.title">Mathtests</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="qsApp.mathtest.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/mathtest/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="qsApp.mathtest.home.createLabel">Create new Mathtest</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {mathtestList && mathtestList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="qsApp.mathtest.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {
                  isAdmin && <th className="hand" onClick={sort('amount')}>
                  Количество <FontAwesomeIcon icon="sort" />
                </th>}
                <th className="hand" onClick={sort('keys')}>
                  Ключевые слова <FontAwesomeIcon icon="sort" />
                </th>
                {/*{*/}
                {/*  isAdmin &&   <th className="hand" onClick={sort('treshold')}>*/}
                {/*  <Translate contentKey="qsApp.mathtest.treshold">Treshold</Translate> <FontAwesomeIcon icon="sort" />*/}
                {/*</th>}*/}
                <th />
              </tr>
            </thead>
            <tbody>
              {mathtestList.map((mathtest, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/mathtest/${mathtest.id}`} color="link" size="sm">
                      {mathtest.id}
                    </Button>
                  </td>
                  {
                    isAdmin && <td>{mathtest.amount}</td>}
                  <td>{mathtest.keys}</td>
                  {/*{*/}
                  {/*  isAdmin &&   <td>{mathtest.treshold}</td>}*/}
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                       <Button tag={Link} to={`/mathtest/${mathtest.id}/quiz`} color="primary" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                          Пройти Тест
                        </span>
                        </Button>

                      {
                        isAdmin && <Button tag={Link} to={`/mathtest/${mathtest.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      }
                      {isAdmin &&
                          <Button
                            tag={Link}
                            to={`/mathtest/${mathtest.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                            color="primary"
                            size="sm"
                            data-cy="entityEditButton"
                          >
                            <FontAwesomeIcon icon="pencil-alt" />{' '}
                            <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                          </Button>
                      }

                      {isAdmin &&
                      <Button
                        tag={Link}
                        to={`/mathtest/${mathtest.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                      }
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="qsApp.mathtest.home.notFound">No Mathtests found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={mathtestList && mathtestList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Mathtest;
