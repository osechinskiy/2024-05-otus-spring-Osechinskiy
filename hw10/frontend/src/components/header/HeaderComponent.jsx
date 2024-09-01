import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
import {Link} from "react-router-dom";

function ResponsiveAppBar() {

    return (
        <AppBar position="static">
            <Container maxWidth="lg">
                <Toolbar disableGutters>
                    <Box sx={{flexGrow: 1, display: {xs: 'none', md: 'flex'}}}>
                        <Button component={Link} to={'/books'} key={'Книги'}
                                sx={{my: 2, color: 'white', display: 'block'}}>
                            {'Книги'}
                        </Button>
                        <Button component={Link} to={'/authors'} key={'Авторы'}
                                sx={{my: 2, color: 'white', display: 'block'}}>
                            {'Авторы'}
                        </Button>
                        <Button component={Link} to={'/genres'} key={'Жанры'}
                                sx={{my: 2, color: 'white', display: 'block'}}>
                            {'Жанры'}
                        </Button>
                    </Box>
                </Toolbar>
            </Container>
        </AppBar>
    );
}

export default ResponsiveAppBar;
