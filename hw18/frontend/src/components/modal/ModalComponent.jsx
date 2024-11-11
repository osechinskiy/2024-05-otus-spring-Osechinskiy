import React from "react";
import Button from "@mui/material/Button";
import {Modal, Stack} from "@mui/material";
import Box from "@mui/material/Box";
import DeleteIcon from '@mui/icons-material/Delete';

const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    pt: 2,
    px: 4,
    pb: 3,
};

const deleteApiData = async (id, action) => {
    await fetch(
        "/api/v1/books/" + id,
        {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
        }
    );
    await getApiData(action)
};

const getApiData = async (setData) => {
    const response = await fetch(
        "/api/v1/books"
    ).then((response) => response.json());
    setData(response);
};

export default function NestedModal(props) {
    const [open, setOpen] = React.useState(false);
    const {id, action} = props;
    const handleOpen = () => {
        setOpen(true);
    };
    const handleClose = () => {
        setOpen(false);
    };

    return (
        <div>
            <Button onClick={handleOpen} variant="contained">Удалить</Button>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="parent-modal-title"
                aria-describedby="parent-modal-description"
            >
                <Box sx={{...style, width: 500}}>
                    <h2 id="parent-modal-title">Подтверждение удаления книги</h2>
                    <p id="parent-modal-description">
                        Вы действительно хотите удалить эту кнугу?<br/>
                        После удаления восстановление будет невозможно!
                    </p>
                    <Stack spacing={2} direction="row">
                        <Button onClick={() => {
                            deleteApiData(id, action).then(r => handleClose())
                        }} variant="contained" startIcon={<DeleteIcon/>}>Удалить</Button>
                        <Button onClick={() => handleClose()} variant="contained" color="error">Отмена</Button>
                    </Stack>
                </Box>
            </Modal>
        </div>
    );
}
