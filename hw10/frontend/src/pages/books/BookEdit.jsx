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

const getBookApiData = async (id) => {
   return await fetch(
        "/api/v1/books/" + id
    ).then((response) => response.json());
};

const getAuthorsApiData = async () => {
   return await fetch(
        "/api/v1/authors"
    ).then((response) => response.json());
};

const getGenresApiData = async () => {
   return await fetch(
        "/api/v1/genres"
    ).then((response) => response.json());
};


const putNewBookApiData = async (book, author, genre, setErrors, navigate) => {
    const response = await fetch(
        "/api/v1/books",
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
        "/api/v1/books",
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
    const [book, setBook] = useState(null);
    const [title, setTitle] = useState(null);
    const [author, setAuthor] = useState(null);
    const [authorData, setAuthorData] = useState(null);
    const [genre, setGenre] = useState(null);
    const [genreData, setGenreData] = useState(null);
    const [errors, setErrors] = useState([]);
    const navigate = useNavigate();


    useEffect(() => {
        const fetchData = async () => {
            if (id) {
                try {
                    const [bookData, authorsData, genresData] = await Promise.all([
                        getBookApiData(id),
                        getAuthorsApiData(),
                        getGenresApiData()
                    ]);

                    setBook(bookData);
                    setAuthorData(authorsData);
                    setGenreData(genresData);
                } catch (error) {
                    console.error("Error fetching data:", error);
                }
            } else {
                try {
                    const [authorsData, genresData] = await Promise.all([
                        getAuthorsApiData(),
                        getGenresApiData()
                    ]);
                    setAuthorData(authorsData);
                    setGenreData(genresData);
                } catch (error) {
                    console.error("Error fetching data:", error);
                }
            }
        };

        fetchData();

    }, [id]);

    console.log(title, author, genre)

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
                            data={book?.title}
                        />
                        <SelectComponent
                            data={authorData}
                            action={setAuthor}
                            sx={{width: 1 / 2}}
                            errors={errors}
                            authorInfo={book?.author}
                        />

                        <MultipleSelectComponent
                            sx={{width: 1 / 2}}
                            data={genreData}
                            action={setGenre}
                            errors={errors}
                            genresInfo={book?.genres}
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