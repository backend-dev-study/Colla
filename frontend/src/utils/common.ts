interface Object {
    [key: string]: string | File | null;
}

export const createFormData = (data: Object) => {
    const formData = new FormData();
    Object.keys(data).forEach((key) => formData.append(key, data[key]!));
    return formData;
};

export const YYYYMMDDToDate = (str: string) => {
    const numStr = str.replace(/[^\d]/g, '');

    const date = new Date(
        parseInt(numStr.slice(0, 4), 10),
        parseInt(numStr.slice(4, 6), 10) - 1,
        parseInt(numStr.slice(6, 8), 10),
    );
    return date;
};
