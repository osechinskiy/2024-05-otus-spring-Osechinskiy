import React, {useState, useEffect} from "react";
import TitleContainer from "../../components/container/TitleContainer.jsx";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import {Stack} from "@mui/material";
import Button from "@mui/material/Button";
import MultipleSelectComponent from "../../components/multipleselect/MultipleSelectComponent.jsx";
import SelectComponent from "../../components/select/SelectComponent.jsx";
import TextInputComponent from "../../components/input/TextInputComponent.jsx";
import {useParams, useNavigate} from "react-router-dom";

const getNewBookApiData = async (setBook) => {
    const response = await fetch(
        "/api/v1/book"
    ).then((response) => response.json());
    setBook(response);
};

const getBookApiData = async (id, setBook) => {
    const response = await fetch(
        "/api/v1/book/" + id
    ).then((response) => response.json());
    setBook(response);
};


const putNewBookApiData = async (book, author, genre, setErrors, navigate) => {
    const response = await fetch(
        "/api/v1/book",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({title: book, authorId: author, genres: genre})
        }
    ).then((response) => response.json());
    setErrors(response);
    if (response.hasError === false) {
        navigate("/books");
    }
};

const putEditBookApiData = async (id, book, author, genre, setErrors, navigate) => {
    const response = await fetch(
        "/api/v1/book",
        {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({id: id, title: book, authorId: author, genres: genre})
        }
    ).then((response) => response.json());
    setErrors(response);
    if (response.hasError === false) {
        navigate("/books");
    }
};

const BookEdit = (props) => {

    const {id} = useParams();
    const [book, setBook] = useState('');
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [genre, setGenre] = useState('');
    const [errors, setErrors] = useState([]);
    const navigate = useNavigate();


    useEffect(() => {
        id ? getBookApiData(id, setBook) : getNewBookApiData(setBook);
    }, [id]);

    return (
        <React.Fragment>
            <TitleContainer text={id ? "Редактирование книги" : "Добавление книги"}/>
            <CssBaseline/>
            <Container maxWidth="lg">
                <Box sx={{bgcolor: 'white', p: 2, borderRadius: 5, mt: 2}}>
                    <Stack spacing={2} sx={{
                        justifyContent: "center",
                        alignItems: "center",
                    }}>
                        <TextInputComponent
                            text={"Название"}
                            action={setTitle}
                            sx={{width: 1 / 2}}
                            errors={errors}
                            data={book.book}
                        />
                        <SelectComponent
                            data={book && book.authors}
                            action={setAuthor}
                            sx={{width: 1 / 2}}
                            errors={errors}
                            authorInfo={book.book && book.book.author}
                        />
                        <MultipleSelectComponent
                            sx={{width: 1 / 2}}
                            data={book.genres}
                            action={setGenre}
                            errors={errors}
                            genresInfo={book.book && book.book.genres}
                        />
                        <Button
                            onClick={() => {
                                id ? putEditBookApiData(id, title, author, genre, setErrors, navigate) :
                                    putNewBookApiData(title, author, genre, setErrors, navigate)
                            }}
                            sx={{width: 1 / 2}}
                            variant="contained">
                            Сохранить
                        </Button>
                    </Stack>
                </Box>
            </Container>
        </React.Fragment>
    )
}

export default BookEdit