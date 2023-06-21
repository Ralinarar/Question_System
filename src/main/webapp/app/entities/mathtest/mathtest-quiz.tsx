import React, {useState, useEffect} from 'react';
import {
  useNavigate, useParams
} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {Translate} from "react-jhipster";
import {Button} from "reactstrap";
import {toast} from "react-toastify";
import {useAppSelector} from "app/config/store";
import {hasAnyAuthority} from "app/shared/auth/private-route";
import {AUTHORITIES} from "app/config/constants";

const QuizComponent = (props) => {
  const [questions, setQuestions] = useState([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [answers, setAnswers] = useState([]);
  const {id} = useParams()
  const navigate = useNavigate()
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));


  useEffect(() => {
    // Fetch questions from the API
    fetch('api/mathtests/quiz/' + id)
      .then(response => response.json())
      .then(data => {
        setQuestions(data);
        if (data.length == 0) {
          toast.warning("Недостаточно данных в онтологии")
          navigate('/mathtest')
        }
        setAnswers(Array(data.length).fill(''));
      })
      .catch(error => {
        console.error(error);
      });
  }, []);

  const handleAnswerChange = (e) => {
    const {value} = e.target;
    const updatedAnswers = [...answers];
    updatedAnswers[currentQuestionIndex] = value;
    setAnswers(updatedAnswers);
  };

  const handleNextQuestion = () => {
    if (currentQuestionIndex < questions.length - 1) {
      setCurrentQuestionIndex(currentQuestionIndex + 1);
    }
  };

  const handleFinishQuiz = () => {
    // Send answers to the backend server
    toast.success("Ответы приняты")
    navigate('/mathtest')

    // fetch('/api/quiz/submit', {
    //   method: 'POST',
    //   headers: {
    //     'Content-Type': 'application/json'
    //   },
    //   body: JSON.stringify(answers)
    // })
    //   .then(response => response.json())
    //   .then(data => {
    //     // Handle the response from the server
    //     console.log(data);
    //   })
    //   .catch(error => {
    //     // Handle any errors that occur during the request
    //     console.error(error);
    //   });
  };

  const currentQuestion = questions[currentQuestionIndex];

  return (
    <div>
      <h1>Тест</h1>
      {questions.length === 0 ? (
        <p>Подгрузка вопросов...</p>
      ) : (
        <div>
          <p>Question {currentQuestionIndex + 1} из {questions.length}:</p>
          {isAdmin && <p><b>Ответы </b></p>}
          <p>{currentQuestion.plain}</p>
          <input
            type="text"
            style={{width: "30%"}}
            onChange={handleAnswerChange}
          />
          <br></br>
          <br></br>
          {isAdmin && <p>Ответ(<b>виден только под ролью администратор</b>)</p>}
          {isAdmin && <input
            type="text"
            style={{width: "30%"}}
            value={isAdmin ? currentQuestion.answer : ""}
            disabled={true}
            onChange={handleAnswerChange}
          />}
          {isAdmin && <br></br>}
          {isAdmin && <br></br>}

          <Button onClick={handleNextQuestion}>
            Следующий вопрос
          </Button>
          {/*<button onClick={handleNextQuestion}>Следующий вопрос</button>*/}
          <br></br><br></br>
          {currentQuestionIndex === questions.length - 1 && (
            // <button onClick={handleFinishQuiz}>Закончить тест</button>
            <Button onClick={handleFinishQuiz}>
              Закончить тест
            </Button>
          )}
        </div>
      )}
    </div>
  );
};

export default QuizComponent;
