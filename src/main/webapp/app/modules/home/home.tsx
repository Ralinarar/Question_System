import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <p className="lead">
          Данный сервис предназначен для автоматического генерирования разнообразных математических вопросов и заданий
          с использованием онтологии математического знания.
        </p>
        <p className="lead">
          Алгоритм для формирования системы вопросов на основе онтологии математического знания является инновационным
          инструментом, который позволяет создавать качественные и разнообразные тестовые задания.
        </p>
        <p className="lead">
          Он основан на применении онтологии профессиональной математики <a href='https://github.com/CLLKazan/OntoMathEdu'>
          OntoMathEdu</a>, которая представляет
          собой формальное описание понятий, связей и правил в математике.
        </p>
        <p className="lead">
          Преимущество данного веб-сервиса представляется в использовании набора данных из базы знаний,
          автоматического создания разнообразных вопросов.
        </p>

        {account?.login ? (
          <div>
            <Alert color="success">
              <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                You are logged in as user {account.login}.
              </Translate>
            </Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>

              <Link to="/login" className="alert-link">
                <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
              </Link>
              <Translate contentKey="global.messages.info.authenticated.suffix">
                , you can try the default accounts:
                <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
                <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
              </Translate>
            </Alert>

            <Alert color="warning">
              <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>&nbsp;
              <Link to="/account/register" className="alert-link">
                <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
              </Link>
            </Alert>
          </div>
        )}
      </Col>
    </Row>
  );
};

export default Home;
