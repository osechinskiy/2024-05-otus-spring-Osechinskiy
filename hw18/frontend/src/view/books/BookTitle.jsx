import React from 'react';
import Container from "@mui/material/Container";
import CssBaseline from "@mui/material/CssBaseline";
import Box from "@mui/material/Box";
import {Stack, Typography} from "@mui/material";
import Button from "@mui/material/Button";
import {Link} from "react-router-dom";

const BookTitle = () => {
    return (
        <React.Fragment>
            <CssBaseline/>
            <Container maxWidth="lg">
                <Box sx={{bgcolor: 'white', p: 2, borderRadius: 5, mt: 2}}>
                    <Stack spacing={2} direction="row" sx={{
                        justifyContent: "space-around",
                        alignItems: "center",
                    }}>
                        <Typography variant="h4" gutterBottom>
                            Информация о книгах
                        </Typography>
                        <Button component={Link} to={"/book/create"} variant="contained">Добавить книгу</Button>
                    </Stack>
                </Box>
            </Container>
        </React.Fragment>
    );
}
export default BookTitle;