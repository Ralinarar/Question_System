import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './predicate.reducer';

export const PredicateDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const predicateEntity = useAppSelector(state => state.predicate.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="predicateDetailsHeading">
          <Translate contentKey="qsApp.predicate.detail.title">Predicate</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{predicateEntity.id}</dd>
          <dt>
            <span id="rdfValue">
              <Translate contentKey="qsApp.predicate.rdfValue">Rdf Value</Translate>
            </span>
          </dt>
          <dd>{predicateEntity.rdfValue}</dd>
          <dt>
            <Translate contentKey="qsApp.predicate.author">Author</Translate>
          </dt>
          <dd>{predicateEntity.author ? predicateEntity.author.login : ''}</dd>
          <dt>
            <Translate contentKey="qsApp.predicate.template">Template</Translate>
          </dt>
          <dd>
            {predicateEntity.templates
              ? predicateEntity.templates.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.mock}</a>
                    {predicateEntity.templates && i === predicateEntity.templates.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/predicate" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/predicate/${predicateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PredicateDetail;
