import React, {useState} from 'react';

const userModal = () => {
    const [open, onOpenModal] = useState(false);
    const [close, onCloseModal] = useState(false);

    const openModal = () => {
        onOpenModal(true);
    };

    const closeModal = () => {
        onCloseModal(true);
        onOpenModal(false);
    };

    return { open, close, openModal, closeModal };
};

export default userModal;